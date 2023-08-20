package com.kspia.sscmservice.common.enums;

public enum Roles {
    ROLE_ADMIN("관리자"), ROLE_USER("사용자"), ROLE_SYSADMIN("총괄 관리자"), ROLE_GUEST("게스트 권한");

    private final String roleNm;

    Roles(String roleNm) { this.roleNm = roleNm; }

    public String getRoleNm() {
        return roleNm;
    }

    public RoleDto toRoleDto() {
        return RoleDto.builder()
                .roleCode(this.name())
                .roleName(this.roleNm)
                .build();
    }
}
