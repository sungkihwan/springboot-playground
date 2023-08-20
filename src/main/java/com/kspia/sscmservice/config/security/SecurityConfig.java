package com.kspia.sscmservice.config.security;

import com.kspia.sscmservice.config.security.jwt.AuthEntryPointJwt;
import com.kspia.sscmservice.config.security.jwt.CustomAccessDeniedHandler;
import com.kspia.sscmservice.config.security.jwt.JwtAuthenticationFilter;
import com.kspia.sscmservice.config.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 과거에는 Security 설정을 WebSecurityConfigurerAdapter 클래스를 상속받아서 구현했지만,
 * Spring Boot 버전이 올라가면서 해당 방식은 Deprecated 되었다.
 * 따라서 이제는 빈 등록을 통해 Security를 설정한다
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtils jwtUtils;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
//                .cors().and()
                .httpBasic().and() // rest api이므로 basic auth 및 (header에 id, pw가 아닌 token(jwt)을 달고 간다. 그래서 basic이 아닌 bearer를 사용한다)
                .csrf().disable() // csrf 보안을 사용하지 않는다는 설정이다.
                .exceptionHandling()
                .authenticationEntryPoint(new AuthEntryPointJwt()) // 인증이 실패한 경우 401 (로그인 자체가 안된 경우)
                .accessDeniedHandler(new CustomAccessDeniedHandler()).and() // 권한이 없는 경우 403
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT를 사용하기 때문에 세션을 사용하지 않는다는 설정
                .and()
                .authorizeRequests()
//                .antMatchers("/auth/*", "/members/exists/*").permitAll() // API 모든 요청을 허가한다는 설정
                .antMatchers(
                        "/sm-sscm-service/user/refresh-token",
                        "/sm-sscm-service/user/login",
                        "/sm-sscm-service/partner/company/hashmap",
                        "/sm-sscm-service/partner/company/search-options"
                ).permitAll()
                .antMatchers("/actuator/**").permitAll()
//                .antMatchers("/sm-sscm-service/user/*").hasRole("ADMIN")
                .anyRequest().authenticated() // 이 밖에 모든 요청에 대해서 인증을 필요로 한다는 설정
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtils), UsernamePasswordAuthenticationFilter.class);
                // JWT 인증을 위하여 JwtAuthenticationFilter 필터를 UsernamePasswordAuthenticationFilter 전에 실행하겠다는 설정

        return http.build();
    }

    // JWT를 사용하기 위해서는 password encoder가 필요한데, Bycrypt encoder 사용
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}