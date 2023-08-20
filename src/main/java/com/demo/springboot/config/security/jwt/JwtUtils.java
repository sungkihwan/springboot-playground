package com.demo.springboot.config.security.jwt;

import com.demo.springboot.config.security.service.CustomUser;
import com.demo.springboot.config.security.service.CustomUserDetails;
import com.demo.springboot.dto.response.TokenResponse;
import com.demo.springboot.entity.User;
import com.demo.springboot.entity.UserRole;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.demo.springboot.config.security.constant.Constants.*;

@Component
@Log4j2
public class JwtUtils {

    private final Key key;

    public JwtUtils(@Value("${token.secret}") String secretKey) throws NoSuchAlgorithmException {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        // SHA-512 알고리즘을 사용하여 해시를 생성
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        byte[] hashedKeyBytes = messageDigest.digest(keyBytes);

        // 해시된 바이트 배열을 SecretKeySpec을 사용하여 비밀키로 변환
        this.key = new SecretKeySpec(hashedKeyBytes, "HmacSHA512");
        log.warn("secretKey = {}", this.key);
    }

    // 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
    public TokenResponse generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date accessTokenExpiresIn = Date.from(now.plus(Duration.ofMillis(ACCESS_TOKEN_EXPIRE_TIME)));
        Date refreshTokenExpiresIn = Date.from(now.plus(Duration.ofMillis(REFRESH_TOKEN_EXPIRE_TIME)));

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(JWT_CLAIMS_ROLE, authorities)
                .claim(JWT_CLAIMS_COMPANY, customUserDetails.getCompanySid())
                .claim(JWT_CLAIMS_ORG_IDX, customUserDetails.getOrgIdx())
                .claim(JWT_CLAIMS_BP_LCD, customUserDetails.getBpLcd())
                .claim(JWT_CLAIMS_GEO_X, customUserDetails.getGeoX())
                .claim(JWT_CLAIMS_GEO_Y, customUserDetails.getGeoY())
                .setIssuedAt(issuedAt)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .setIssuedAt(issuedAt)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenResponse.builder()
                .grantType(JWT_PREFIX)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(JWT_CLAIMS_ROLE) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        String[] rolesArray = claims.get(JWT_CLAIMS_ROLE, String.class).split(",");
        List<String> roles = Arrays.asList(rolesArray);

        Collection<? extends GrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // 클레임에서 회사 정보 가져오기
        String companySid = claims.get(JWT_CLAIMS_COMPANY, String.class);
        String orgIdx = claims.get(JWT_CLAIMS_ORG_IDX, String.class);
        String geoX = claims.get(JWT_CLAIMS_GEO_X, String.class);
        String geoY = claims.get(JWT_CLAIMS_GEO_Y, String.class);
        String bpLcd = claims.get(JWT_CLAIMS_BP_LCD, String.class);

        // UserDetails 객체를 만들어서 Authentication 리턴
//        UserDetails principal = new User(claims.getSubject(), "", authorities);
        CustomUserDetails principal = new CustomUser(claims.getSubject(), "", roles,
                companySid, 0, orgIdx, geoX, geoY, bpLcd);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public TokenResponse refreshToken(User user){

        List<String> roles = user.getUserRoles()
                .stream()
                .map(UserRole::getRoleCd)
                .collect(Collectors.toList());

        String authorities = String.join(",", roles);
        Long companySid = Objects.nonNull(user.getCompany()) ? user.getCompany().getCompanySid() : 313519L;

        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date accessTokenExpiresIn = Date.from(now.plus(Duration.ofMillis(ACCESS_TOKEN_EXPIRE_TIME)));
        Date refreshTokenExpiresIn = Date.from(now.plus(Duration.ofMillis(REFRESH_TOKEN_EXPIRE_TIME)));

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(user.getEmail())
                .claim(JWT_CLAIMS_ROLE, authorities)
                .claim(JWT_CLAIMS_COMPANY, Long.toString(companySid))
                .claim(JWT_CLAIMS_ORG_IDX, String.valueOf(user.getOrgIdx()))
                .claim(JWT_CLAIMS_BP_LCD, user.getBpLcd())
                .claim(JWT_CLAIMS_GEO_X, user.getGeoX())
                .claim(JWT_CLAIMS_GEO_Y, user.getGeoY())
                .setIssuedAt(issuedAt)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .setIssuedAt(issuedAt)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return TokenResponse.builder()
                .grantType(JWT_PREFIX)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public static Long getCompanySid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return Long.parseLong(userDetails.getCompanySid());
        } else {
            throw new RuntimeException("인증 정보가 없거나 회사 정보를 가져올 수 없습니다.");
        }
    }

    public static Long getOrgIdx() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return Long.parseLong(userDetails.getOrgIdx());
        } else {
            throw new RuntimeException("인증 정보가 없거나 지역 정보를 가져올 수 없습니다.");
        }
    }

    public static String getX() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getGeoX();
        } else {
            throw new RuntimeException("인증 정보가 없거나 X 좌표를 가져올 수 없습니다.");
        }
    }

    public static String getGeoY() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getGeoY();
        } else {
            throw new RuntimeException("인증 정보가 없거나 Y 좌표를 가져올 수 없습니다.");
        }
    }

    public static String getBpLcd() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getBpLcd();
        } else {
            throw new RuntimeException("인증 정보가 없거나 회사 정보를 가져올 수 없습니다.");
        }
    }
}
