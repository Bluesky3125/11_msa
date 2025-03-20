package com.ohgiraffers.userservice.dto;

import lombok.Data;

@Data
public class UserDTO {	// 계층을 오가며 BL을 진행할 때 필요한 값
	private String email;
	private String name;
	private String pwd;

	/* 설명. 회원가입 진행하며 추가됨 */
	private String userId;
}
