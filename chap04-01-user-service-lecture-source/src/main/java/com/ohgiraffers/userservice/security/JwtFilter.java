package com.ohgiraffers.userservice.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {
	
	private final JwtUtil jwtUtil;
	
	public JwtFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
		ServletException,
		IOException {
		String token = request.getHeader("token");
		log.info("들고 온 토큰 확인: {}", token);
		
		if (token != null && token.startsWith("Bearer ")) {
			token = token.substring(7);	// "Bearer "를 제외한 토큰 부분만 추출
			log.info("순수 토큰 값: {}", token);
		}
		if (jwtUtil.validateToken(token)) {
			
			/* 설명. 유효한 토큰을 통해 아이디와 권한들을 가진 Authentication 추출 */
			Authentication authentication = jwtUtil.getAuthentication(token);
			
			/* 설명. Spring Security가 인식할 수 있게 주입(LocalThread에 저장) */
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		/* 설명. 다음 필터를 진행하게 해야 AuthenticationFilter가 동작함 */
		filterChain.doFilter(request, response);
	}
}
