package com.ohgiraffers.userservice.infrastructure;

import org.apache.http.HttpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Configuration
public class FeignClientConfig {
	
	@Bean
	public RequestInterceptor requestInterceptor() {
		return new RequestInterceptor() {
			@Override
			public void apply(RequestTemplate requestTemplate) {
				
				/* 설명. 현재 요청의 ServletRequest를 가져옴 */
				ServletRequestAttributes requestAttributes =
					(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
				
				if (requestAttributes != null) {
					String authorizationHeader = requestAttributes.getRequest()
																  .getHeader(HttpHeaders.AUTHORIZATION);
					if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
						requestTemplate.header(HttpHeaders.AUTHORIZATION, authorizationHeader);
					}
				}
			}
		};
	}
}
