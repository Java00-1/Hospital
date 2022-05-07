package com.atguigu.yygh.controller;

import com.atguigu.result.Result;
import com.atguigu.yygh.service.UserInfoService;
import com.atguigu.yygh.vo.user.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserInfoApiController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo){
        Map<String,Object> map = userInfoService.login(loginVo);
        return Result.ok(map);
    }


}
