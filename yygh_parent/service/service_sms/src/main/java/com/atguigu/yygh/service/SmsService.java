package com.atguigu.yygh.service;

import org.springframework.stereotype.Repository;

@Repository
public interface SmsService {
    boolean send(String phone, String code);
}
