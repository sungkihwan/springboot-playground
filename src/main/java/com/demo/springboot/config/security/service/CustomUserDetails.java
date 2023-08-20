package com.demo.springboot.config.security.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails {
    String getCompanySid();
    Integer getLoginCount();
    String getOrgIdx();
    String getGeoX();
    String getGeoY();
    String getBpLcd();
}


