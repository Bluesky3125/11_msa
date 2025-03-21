package com.ohgiraffers.orderservice.aggregate;

import java.util.List;

import lombok.Data;

@Data
public class Order {	// resultMap을 여러 개 만들기, entity/DTO는 하나만
	private int orderCode;
	private int userId;
	private String orderDate;
	private String orderTime;
	private int totalOrderPrice;
	
	private List<OrderMenu> orderMenus;
}
