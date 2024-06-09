package com.alivold.service;

import com.alivold.config.BaseException;
import com.alivold.domain.SysUser;
import com.alivold.resp.ResponseResult;
import org.springframework.http.ResponseEntity;

public interface LoginService {
    ResponseEntity<ResponseResult> login(SysUser sysUser);

    ResponseResult logout(String token);
}
