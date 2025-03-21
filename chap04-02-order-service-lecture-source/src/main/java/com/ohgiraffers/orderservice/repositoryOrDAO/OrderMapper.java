package com.ohgiraffers.orderservice.repositoryOrDAO;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ohgiraffers.orderservice.aggregate.Order;

@Mapper
public interface OrderMapper {
	List<Order> selectOrderByUserId(int userId);
}
