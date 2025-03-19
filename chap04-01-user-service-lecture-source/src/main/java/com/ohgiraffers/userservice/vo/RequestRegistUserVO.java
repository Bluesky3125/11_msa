package com.ohgiraffers.userservice.vo;

import lombok.Data;

@Data
public class RequestRegistUserVO {
    private String email;
    private String pwd;
    private String name;
}
