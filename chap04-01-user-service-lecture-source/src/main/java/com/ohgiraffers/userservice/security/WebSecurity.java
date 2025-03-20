package com.ohgiraffers.userservice.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.Filter;

/* 설명. WebSecurityConfigurerAdapter를 상속받거나 @EnableWebSecurity를 쓰는 예제는 옛날 방식 */
@Configuration
// @EnableWebSecurity
public class WebSecurity {
	
	private final JwtAuthenticationProvider jwtAuthenticationProvider;
	
	@Autowired
	public WebSecurity(JwtAuthenticationProvider jwtAuthenticationProvider) {
		this.jwtAuthenticationProvider = jwtAuthenticationProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(Collections.singletonList(jwtAuthenticationProvider));
	}
	
	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable);
		
		/* 설명. 허용되는 경로 및 권한 설정 */
		http.authorizeHttpRequests(authz ->
			/* 설명. 일단 permitAll로 들어갈 수 있게 한 후 나중에 권한 변경 */
			/* 설명. 401 인증 필요, 403 인가 필요 */
			authz.requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
				 .anyRequest().authenticated()
			)
			.authenticationManager(authenticationManager())
			.sessionManagement(session ->
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			);
		
		http.addFilter(getAuthenticationFilter(authenticationManager()));
		
		return http.build();
	}
	
	private Filter getAuthenticationFilter(AuthenticationManager authenticationManager) {
		return new AuthenticationFilter(authenticationManager);
	}
}
