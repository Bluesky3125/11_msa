package com.ohgiraffers.eurekaclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient  // 없어도 되긴 하지만 명시적으로 작성
public class Chap0102EurekaClientLectureSourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Chap0102EurekaClientLectureSourceApplication.class, args);
    }

}
