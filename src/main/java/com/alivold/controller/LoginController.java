package com.alivold.controller;

import com.alibaba.fastjson.JSONObject;
import com.alivold.config.BaseException;
import com.alivold.domain.SysUser;
import com.alivold.resp.ResponseResult;
import com.alivold.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody JSONObject jsonObject) {
        return loginService.login(jsonObject);
    }

    @PostMapping("/logout")
    public ResponseResult logout(){
        return loginService.logout();
    }
}
