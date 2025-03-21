package com.ohgiraffers.userservice.dto;

import java.util.List;

import com.ohgiraffers.userservice.vo.ResponseOrder;

import lombok.Data;

@Data
public class UserDTO {	// 계층을 오가며 BL을 진행할 때 필요한 값
	private String email;
	private String name;
	private String pwd;

	/* 설명. 회원가입 진행하며 추가됨 */
	private String userId;
	
	/* 설명. FeignClient 이후(회원이 주문한 내역도 담기) */
	private List<ResponseOrder> orders;
}
