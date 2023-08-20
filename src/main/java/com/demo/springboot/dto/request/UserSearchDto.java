package com.demo.springboot.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @since 2023. 03. 09
 * @author jung
 *
 * @Discript
 * ---------------------------------------------------
 * 개요 : SmUserSearchDto 작성
 * ---------------------------------------------------
 * @EditHIstory
 * 개정이력
 * 2023-03.09 jung: 최초 작성
 * 2023-03.09 jung : SmUserSearchDto 작성
 */


@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class UserSearchDto {
	//사용자 이메일
	private String email;
	//사용자 이름
	private String name;
	//소속
	private String department;
	//소속
	private String position;
	//권한그룹 코드
	private String roleCode;
//	private Roles roleCode;
	//활성화 여부
	private String activated;
}


