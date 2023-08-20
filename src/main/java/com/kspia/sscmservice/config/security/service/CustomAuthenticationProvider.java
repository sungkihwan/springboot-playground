package com.kspia.sscmservice.config.security.service;

import com.kspia.sscmservice.repository.querydsl.UserCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("authenticationProvider")
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailsService userDetailsService;

    private final UserCustomRepository userCustomRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            userCustomRepository.updateLoginFailCount(username);
            if (userDetails.getLoginCount() == 4) {
                userCustomRepository.updateActivatedStatus(username, false);
                throw new BadCredentialsException("잘못된 패스워드를 5회이상 입력하여 계정이 잠금되었습니다.");
            } else {
                throw new BadCredentialsException("패스워드를 잘못입력했습니다. 현재 : " + userDetails.getLoginCount() + "회");
            }
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

