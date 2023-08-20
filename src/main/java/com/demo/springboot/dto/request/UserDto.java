package com.demo.springboot.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.util.List;

/**
 * @since 2023. 03. 09
 * @author jung
 *
 * @Discript
 * ---------------------------------------------------
 * 개요 : SmUserDto작성
 * ---------------------------------------------------
 * @EditHIstory
 * 개정이력
 * 2023-03.09 jung: 최초 작성
 * 2023-03.09 jung : SmUserDto작성
 */

@Data
@NoArgsConstructor
public class UserDto {

	@Email
	private String email;
	private String name;
	private Boolean activated;
	private Boolean accessWeb;
	private Boolean accessMobile;
	private Long companySid;

//	@EachEnum(enumClass = Roles.class)
	private List<String> assignedRoles;

	private String latestUpdater;
	private String ipAddress;
	private Integer loginCount;

	private Long orgIdx;
	private String geoX;
	private String geoY;
	private String zoom;
	private String dashboardDefaultSelect;
	private String timeLimitCd;
	private String password;
	private String passwordCompare;
	private List<String> bookMarkList;

	@Builder
	public UserDto(String email, String name, Boolean activated, Boolean accessWeb, Boolean accessMobile, Long companySid, List<String> assignedRoles, String latestUpdater, String ipAddress, Integer loginCount, Long orgIdx, String geoX, String geoY, String zoom, String dashboardDefaultSelect, String timeLimitCd, String password, String passwordCompare, List<String> bookMarkList) {
		this.email = email;
		this.name = name;
		this.activated = activated;
		this.accessWeb = accessWeb;
		this.accessMobile = accessMobile;
		this.companySid = companySid;
		this.assignedRoles = assignedRoles;
		this.latestUpdater = latestUpdater;
		this.ipAddress = ipAddress;
		this.loginCount = loginCount;
		this.orgIdx = orgIdx;
		this.geoX = geoX;
		this.geoY = geoY;
		this.zoom = zoom;
		this.dashboardDefaultSelect = dashboardDefaultSelect;
		this.timeLimitCd = timeLimitCd;
		this.password = password;
		this.passwordCompare = passwordCompare;
		this.bookMarkList = bookMarkList;
	}
}
