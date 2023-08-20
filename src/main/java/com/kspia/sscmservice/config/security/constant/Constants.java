package com.kspia.sscmservice.config.security.constant;

public class Constants {
    public static final Long ACCESS_TOKEN_EXPIRE_TIME = 600 * 30 * 60 * 1000L; // 30분
    public static final Long REFRESH_TOKEN_EXPIRE_TIME = 9 * 60 * 60 * 1000L; // 9시간
    public static final String JWT_PREFIX = "Bearer";
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_CLAIMS_ROLE = "roles";
    public static final String JWT_CLAIMS_COMPANY = "company";
    public static final String JWT_CLAIMS_ORG_IDX = "ordIdx";
    public static final int USER_LOGIN_FAIL_LOCK_COUNT = 5;
    public static final String JWT_CLAIMS_GEO_X = "geoX";
    public static final String JWT_CLAIMS_GEO_Y = "geoY";
    public static final String JWT_CLAIMS_BP_LCD = "bpLcd";
}
