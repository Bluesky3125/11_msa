package com.ohgiraffers.userservice.infrastructure;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.ohgiraffers.userservice.vo.ResponseOrder;

/* 설명. order-service와의 통신을 위한 클래스 */
/* 목차.
 *  1. FeignClient 관련 라이브러리 추가
 *  2. main 가서 어노테이션 추가할 것(@EnableFeignClients)
 * */
// @FeignClient(name="swcamp-order-service", url="localhost:8000")
/* 설명. user-service가 order-service로 갈 때 token을 들고 가도록 설정을 만들어 추가 */
@FeignClient(name="swcamp-order-service", url="localhost:8000", configuration = FeignClientConfig.class)
public interface OrderServiceClient {
	@GetMapping("/order-service/users/{userId}/orders")
	List<ResponseOrder> getUserOrders(@PathVariable String userId);
}
