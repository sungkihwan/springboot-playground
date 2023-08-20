package com.demo.springboot.dto.response;

/**
 * 클라이언트에 토큰을 보내기 위한 DTO
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class TokenResponse {

    private String grantType;       // JWT 대한 인증 타입 (Bearer 사용)
    private String accessToken;
    private String refreshToken;
}
