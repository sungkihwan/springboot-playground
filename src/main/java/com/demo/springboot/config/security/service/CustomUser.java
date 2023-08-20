package com.demo.springboot.config.security.service;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class CustomUser implements CustomUserDetails {

    private String email;
    private String password;
    private List<String> roles;
    private String companySid;
    private Integer loginCount;
    private String orgIdx;
    private String geoX;
    private String geoY;
    private String bpLcd;

    public CustomUser(String email, String password, List<String> roles, String companySid,
                      Integer loginCount, String orgIdx, String geoX, String geoY, String bpLcd) {
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.companySid = companySid;
        this.loginCount = loginCount;
        this.orgIdx = orgIdx;
        this.geoX = geoX;
        this.geoY = geoY;
        this.bpLcd = bpLcd;
    }

    // 커스텀 필드를 구현
    @Override
    public String getCompanySid() {
        return companySid;
    }

    @Override
    public Integer getLoginCount() {
        return loginCount;
    }

    @Override
    public String getOrgIdx() {
        return orgIdx;
    }

    // UserDetails의 메소드를 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getGeoX() {
        return this.geoX;
    }

    @Override
    public String getGeoY() {
        return this.geoY;
    }
    @Override
    public String getBpLcd() {
        return bpLcd;
    }
}

