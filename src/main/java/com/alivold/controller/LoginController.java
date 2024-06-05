package com.alivold.controller;

import com.alibaba.fastjson.JSONObject;
import com.alivold.config.BaseException;
import com.alivold.domain.SysUser;
import com.alivold.resp.ResponseResult;
import com.alivold.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody SysUser sysUser) {
        return loginService.login(sysUser);
    }

    @PostMapping("/logout")
    public ResponseResult logout(@RequestHeader Map<String, String> headers){
        String token = headers.get("token");
        return loginService.logout(token);
    }
}
