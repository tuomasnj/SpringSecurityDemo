package com.alivold.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.alivold.authentication.SmsCodeAuthenticationToken;
import com.alivold.config.BaseException;
import com.alivold.domain.LoginUser;
import com.alivold.domain.SysUser;
import com.alivold.resp.ResponseResult;
import com.alivold.service.LoginService;
import com.alivold.util.JwtUtil;
import com.alivold.util.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.xml.ws.Response;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    private static final String LOGIN_KEY = "login_user";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(JSONObject jsonObject) {
        //进行用户认证
        Authentication authenticate = null;
        try{
            SmsCodeAuthenticationToken authenticationToken = new SmsCodeAuthenticationToken(jsonObject.getString("phoneNumber"), jsonObject.getString("smsCode"));
            //authenticate是Authentication类型的，验证成功以后，authenticate携带了更为丰富的用户详细信息以及权限信息。
            authenticate = authenticationManager.authenticate(authenticationToken);
        }catch (Exception e){
            e.printStackTrace();
            throw new BaseException("服务异常");
        }
        //如果用户认证失败，提示
        if(ObjectUtil.isNull(authenticate)){
            return ResponseResult.fail("登陆失败");
        }
        //获取用户信息，根据userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        Long userId = loginUser.getUser().getId();
        String jwt = JwtUtil.createJwt(userId.toString());

        //存入redis
        redisCache.setCacheObject(LOGIN_KEY + userId.toString(), loginUser.getUser(), 60* 60);
        JSONObject res = new JSONObject();
        res.put("token", jwt);
        return ResponseResult.success(res);
    }

    @Override
    public ResponseResult logout() {
        //获取SecurityContextHolder中的用户id
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authenticationToken.getPrincipal();
        String userId = loginUser.getUser().getId().toString();

        //删除redis中的用户信息
        redisCache.deleteObject(LOGIN_KEY + userId);
        return ResponseResult.success();
    }
}
