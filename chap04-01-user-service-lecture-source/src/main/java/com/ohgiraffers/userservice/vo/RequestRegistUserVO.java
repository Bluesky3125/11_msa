package com.ohgiraffers.userservice.vo;

import lombok.Data;

@Data
public class RequestRegistUserVO {  // 요청 값을 받는 것 (Validation check)
    private String email;
    private String pwd;
    private String name;
}
