package com.ohgiraffers.orderservice.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ohgiraffers.orderservice.dto.OrderDTO;
import com.ohgiraffers.orderservice.service.OrderService;
import com.ohgiraffers.orderservice.vo.ResponseOrderVO;

@RestController
public class OrderController {
	private final OrderService orderService;
	
	@Autowired
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}
	
	@GetMapping("/users/{userId}/orders")	// RestAPI 방식: url 경로만 봐도 무슨 일인지 알 수 있는 구조, 순서도 중요
	public ResponseEntity<List<ResponseOrderVO>> getUserOrders(@PathVariable int userId) {
		List<OrderDTO> orderDTOList = orderService.getOrderByUserId(userId);
		List<ResponseOrderVO> returnValue = orderDTOToResponseOrder(orderDTOList);
		
		return ResponseEntity.ok().body(returnValue);
	}
	
	private List<ResponseOrderVO> orderDTOToResponseOrder(List<OrderDTO> orderDTOList) {
		
		List<ResponseOrderVO> responseList = new ArrayList<>();
		
		for (OrderDTO orderDTO : orderDTOList) {
			ResponseOrderVO responseOrder = new ResponseOrderVO();
			responseOrder.setOrderDate(orderDTO.getOrderDate());
			responseOrder.setOrderTime(orderDTO.getOrderTime());
			responseOrder.setOrderMenus(orderDTO.getOrderMenus());
			
			responseList.add(responseOrder);
		}
		
		return responseList;
	}
}
