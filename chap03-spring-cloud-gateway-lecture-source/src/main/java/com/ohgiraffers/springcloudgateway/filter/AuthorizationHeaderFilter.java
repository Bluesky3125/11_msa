package com.ohgiraffers.springcloudgateway.filter;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {
	
	Environment env;
	
	@Autowired
	public AuthorizationHeaderFilter(Environment env) {
		super(Config.class);
		this.env = env;
	}
	
	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			
			/* 설명. request header에 "Authorization"이라는 키 값이 없다면 토큰 없이 요청이 온 것으로 판단 */
			if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				return onError(exchange, "No Authorization Header", HttpStatus.UNAUTHORIZED);
			}
			
			/* 설명. 토큰을 들고 왔다면 추가 검증 */
			HttpHeaders headers = request.getHeaders();		// apache말고 springframework 패키지로 import해야 한다.
			
			/* 설명. request header에 담긴 값들 로그로 확인 */
			Set<String> keys = headers.keySet();
			log.info(">>>");
			keys.stream().forEach(key -> log.info(key + " : " + request.getHeaders().get(key)));
			log.info("<<<");
			
			/* 설명. "Authorization"이라는 키 값으로 request header에 담긴 값 추출(사용자가 들고 온 JWT) */
			String bearerToken = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			String jwt = bearerToken.replace("Bearer ", "");
			
			if(!isJwtValid(jwt)) {
				return onError(exchange, "Invalid JWT token", HttpStatus.UNAUTHORIZED);
			}
			
			return chain.filter(exchange);
		};
	}
	
	private boolean isJwtValid(String jwt) {
		boolean returnValue = true;
		
		String subject = null;
		
		/* 설명. 기본적으로 우리 서버에서 만든 토큰인지와, 토큰 안에 subject라는 등록된 클레임이 있는지 확인 */
		try {
			subject = Jwts.parserBuilder()
				.setSigningKey(env.getProperty("token.secret"))
				.build()
				.parseClaimsJws(jwt)
				.getBody()
				.getSubject();
		} catch (Exception e) {
			returnValue = false;
		}
		
		if (subject == null || subject.isEmpty()) {
			returnValue = false;
		}
		
		return returnValue;
	}
	
	/* 설명. Mono는 아무 데이터도 반환하지 않고, 비동기적으로 완료됨을 나타내는 반환 타입 */
	private Mono<Void> onError(ServerWebExchange exchange, String errorMessage, HttpStatus httpStatus) {
		
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		log.info("에러 메세지: {}", errorMessage);
		
		return response.setComplete();
	}
	
	public static class Config {
	
	}
}
