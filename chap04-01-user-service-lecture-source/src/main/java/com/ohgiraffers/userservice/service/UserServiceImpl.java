package com.ohgiraffers.userservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ohgiraffers.userservice.aggregate.UserEntity;
import com.ohgiraffers.userservice.dto.UserDTO;
import com.ohgiraffers.userservice.repository.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
	UserRepository userRepository;
	ModelMapper modelMapper;
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository
						 , ModelMapper modelMapper
					 	 , BCryptPasswordEncoder bCryptPasswordEncoder
	) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	@Override
	@Transactional
	public void registUser(UserDTO userDTO) {
		
		/* 설명. 회원가입할 때 고유번호 할당 */
		userDTO.setUserId(UUID.randomUUID().toString());
		
		/* 설명. Entity로 modelMapper로 매핑 후 엔티티에 있는 encryptedPwd에 암호화된 값을 추가한다. */
		UserEntity registUser = modelMapper.map(userDTO, UserEntity.class);
		registUser.setEncryptedPwd(bCryptPasswordEncoder.encode(userDTO.getPwd()));	// plain text(평문) ->
		
		userRepository.save(registUser);
	}
	
	/* 설명. spring security 사용 시 프로바이더에서 활용할 로그인용 메소드(id로 회원 조회해서 UserDetails 타입을 반환하는 메소드) */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		UserEntity loginUser = userRepository.findByEmail(email);	// email 필드로 where절을 걸어서 조회하는 쿼리 메소드
		
		/* 설명. 사용자가 로그인 시 id(이메일)를 잘못 입력했다면 */
		if (loginUser == null) {
			throw new UsernameNotFoundException(email + " 이메일 아이디의 유저는 존재하지 않습니다.");
		}
		
		/* 설명. DB에서 조회 된 해당 email의 회원 권한들을 가져와 List<GrantedAuthority>로 만듦 */
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ENTERPRISE"));
		
		return new User(loginUser.getEmail(), loginUser.getEncryptedPwd(),
			true, true, true, true, grantedAuthorities);
	}
}
