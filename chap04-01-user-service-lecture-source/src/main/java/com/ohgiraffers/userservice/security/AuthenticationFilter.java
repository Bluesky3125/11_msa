package com.ohgiraffers.userservice.security;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohgiraffers.userservice.vo.RequestLoginVO;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}
	
	/* 설명. 로그인 시도 시 동작하는 기능(POST /login 요청 시) */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
		throws AuthenticationException {
		try {
			/* 설명. request를 통해 넘어온 json(login 시 id/pwd)를 RequestLoginVO 옮겨담기 */
			RequestLoginVO creds = new ObjectMapper().readValue(request.getInputStream(), RequestLoginVO.class);
			
			return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPwd(), new ArrayList<>())
			);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/* 설명. 로그인 성공 시(Authentication authResult에는 로그인에 성공한 User 객체가 들어있음) JWT 토큰 생성할 예정 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request,
											HttpServletResponse response,
											FilterChain chain,
											Authentication authResult) {
		log.info("로그인 성공 이후 spring security가 Authentication 객체로 관리되어 넘어옴: {}", authResult);
	}
}
