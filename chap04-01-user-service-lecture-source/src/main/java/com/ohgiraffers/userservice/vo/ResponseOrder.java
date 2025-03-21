package com.ohgiraffers.userservice.vo;

import java.util.List;

import lombok.Data;

@Data
public class ResponseOrder {
	private String orderDate;
	private String orderTime;
	
	private List<OrderMenu> orderMenus;
}
