package com.ohgiraffers.userservice.security;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.ohgiraffers.userservice.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {
	
	private final Key key;
	private final UserService userService;
	
	@Autowired
	public JwtUtil(
		@Value(value = "${token.secret}") String secretKey,
		UserService userService
	) {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.userService = userService;
	}
	
	/* 설명. Token 검증(Bearer 토큰이 넘어왔는지, 우리 사이트의 secret key로 만들어졌는지, 만료되었는지, 내용이 비어있진 않은지) */
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.error("유효하지 않은 JWT Token");
		} catch (ExpiredJwtException e) {
			log.error("만료기간이 지남");
		} catch (UnsupportedJwtException e) {
			log.info("지원하지 않는 JWT Token");
		} catch (IllegalArgumentException e) {
			log.info("토큰의 클레임이 비어있음");
		}
		return false;
	}
	
	public Authentication getAuthentication(String token) {
		
		Claims claims = parseClaims(token);
		
		/* 설명. 토큰에 들어있는 이메일로 DB에서 회원 조회하고 UserDetails로 가져옴 */
		UserDetails userDetails = userService.loadUserByUsername(claims.getSubject());
		
		/* 설명. 토큰에 들어있는 권한들을 List<GrantedAuthority> 타입으로 */
		Collection<GrantedAuthority> authorities;
		if (claims.get("auth") == null) {	// 권한이 없으면
			throw new RuntimeException("권한 정보가 없는 토큰입니다.");
		} else {							// 권한이 있으면
			authorities =
				Arrays.stream(claims.get("auth").toString()
									.replace("[", "")
									.replace("]", "")
									.split(", ")
							 )
					  .map(SimpleGrantedAuthority::new)
					  .collect(Collectors.toList())
				;
		}
		return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
	}
	
	private Claims parseClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}
}
