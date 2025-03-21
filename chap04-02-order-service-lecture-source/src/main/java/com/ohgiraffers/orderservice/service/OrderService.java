package com.ohgiraffers.orderservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ohgiraffers.orderservice.dto.OrderDTO;

@Service
public interface OrderService {
	List<OrderDTO> getOrderByUserId(int userId);
}
