package com.demo.springboot.service;

import com.demo.springboot.config.security.constant.Constants;
import com.demo.springboot.config.security.jwt.JwtUtils;
import com.demo.springboot.entity.User;
import com.demo.springboot.repository.UserRepository;
import com.demo.springboot.repository.UserRoleRepository;
import com.demo.springboot.repository.querydsl.RoleCustomRepository;
import com.demo.springboot.dto.request.UserDto;
import com.demo.springboot.dto.response.TokenRefresh;
import com.demo.springboot.dto.response.TokenResponse;
import com.demo.springboot.repository.querydsl.UserCustomRepository;
import com.kspia.sscmservice.entity.*;
import com.kspia.sscmservice.repository.querydsl.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {
    private final UserRepository userRepository;
    private final UserCustomRepository userCustomRepository;
    private final RoleCustomRepository roleCustomRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtUtils jwtUtils;

    @Transactional
    public TokenResponse login(UserDto userDto) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword());

        // 2. 사용자 비밀번호 체크
        // authenticate 매서드가 실행될 때 CustomMemberDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenResponse tokenResponse = jwtUtils.generateToken(authentication);
        userRepository.updateRefreshToken(userDto.getEmail(), tokenResponse.getRefreshToken());

        return tokenResponse;
    }

//    @Transactional
//    public UserManageResponse signup(UserDto userDto) {
//
//        if (userCustomRepository.existsByEmail(userDto.getEmail())) {
//            throw new AlreadyExistsException("이미 존재하는 이메일입니다.");
//        }
//
//        userRoleRepository.deleteByEmail(userDto.getEmail());
//
//        Long companySid = Optional.ofNullable(userDto.getCompanySid()).orElse(0L);
//
//        User user = User.builder()
//                .email(userDto.getEmail())
//                .passwd(passwordEncoder.encode(userDto.getPassword()))
//                .orgIdx(userDto.getOrgIdx())
//                .userNm(userDto.getName())
//                .build();
//
//        user = userRepository.save(user);
//
//        if (userDto.getAssignedRoles() != null && !userDto.getAssignedRoles().isEmpty()) {
//            List<Role> roles = roleCustomRepository.findByRoleCdIn(userDto.getAssignedRoles());
//            Set<UserRole> userRoles = new HashSet<>();
//            for (Role role : roles) {
//                UserRole userRole = UserRole.builder()
//                        .email(userDto.getEmail())
//                        .roleCd(role.getRoleCd())
//                        .build();
//
//                userRoles.add(userRole);
//            }
//            user.setUserRoles(userRoles);
//            userRoleRepository.saveAll(userRoles);
//        }
//
//        return UserManageResponse.fromSmUser(user);
//    }

    @Transactional
    public TokenResponse refreshToken(TokenRefresh request) {

        jwtUtils.validateToken(request.getRefreshToken());

        User user = userRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

        if (user.getDeleted() || user.getActivated().equals(false) || user.getLoginCount() >= Constants.USER_LOGIN_FAIL_LOCK_COUNT) {
            throw new BadCredentialsException("계정이 잠금되었습니다.");
        }

        TokenResponse tokenResponse = jwtUtils.refreshToken(user);

        userRepository.updateRefreshToken(user.getEmail(), tokenResponse.getRefreshToken());

        return tokenResponse;
    }
}
