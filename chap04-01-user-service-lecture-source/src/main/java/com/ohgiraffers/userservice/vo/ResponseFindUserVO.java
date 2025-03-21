package com.ohgiraffers.userservice.vo;

import java.util.List;

import lombok.Data;

@Data
public class ResponseFindUserVO {
	private String email;
	private String name;
	private String userId;
	
	/* 설명. FeignClient 이후(주문 내역도 담기) */
	private List<ResponseOrder> orders;
}
