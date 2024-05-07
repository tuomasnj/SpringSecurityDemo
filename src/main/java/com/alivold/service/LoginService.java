package com.alivold.service;

import com.alibaba.fastjson.JSONObject;
import com.alivold.config.BaseException;
import com.alivold.domain.SysUser;
import com.alivold.resp.ResponseResult;

public interface LoginService {
    ResponseResult login(JSONObject jsonObject);

    ResponseResult logout();
}
