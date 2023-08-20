package com.kspia.sscmservice.config.security.service;

import com.kspia.sscmservice.common.exception.DeletedUserException;
import com.kspia.sscmservice.entity.User;
import com.kspia.sscmservice.entity.UserRole;
import com.kspia.sscmservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.kspia.sscmservice.config.security.constant.Constants.USER_LOGIN_FAIL_LOCK_COUNT;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

        if (user.getDeleted()) {
            throw new DeletedUserException("삭제된 유저입니다.");
        }

        if (user.getLoginCount() >= USER_LOGIN_FAIL_LOCK_COUNT || !user.getActivated()) {
            throw new BadCredentialsException("계정이 잠금되었습니다.");
        }

        return createUserDetails(user);
    }

    // 해당하는 User 의 데이터가 존재한다면 CustomUserDetails 객체로 만들어서 리턴
    private CustomUserDetails createUserDetails(User user) {
        List<String> roles = user.getUserRoles()
                .stream()
                .map(UserRole::getRoleCd)
                .collect(Collectors.toList());


        return CustomUser.builder()
                .email(user.getEmail())
                .password(user.getPasswd())
                .roles(roles)
                .orgIdx(String.valueOf(user.getOrgIdx()))
                .loginCount(user.getLoginCount())
                .geoX(user.getGeoX())
                .geoY(user.getGeoY())
                .build();
    }
}
