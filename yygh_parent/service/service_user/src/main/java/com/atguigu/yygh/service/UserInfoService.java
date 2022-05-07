package com.atguigu.yygh.service;

import com.atguigu.yygh.model.user.UserInfo;
import com.atguigu.yygh.vo.user.LoginVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.Map;


public interface UserInfoService extends IService<UserInfo> {
    Map<String, Object> login(LoginVo loginVo);

    UserInfo selectWxInfoOpenId(String openid);
}
