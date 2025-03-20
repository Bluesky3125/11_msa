package com.ohgiraffers.userservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.ohgiraffers.userservice.dto.UserDTO;

public interface UserService extends UserDetailsService {
    void registUser(UserDTO userDTO);
}
