package com.alivold.handler;
import com.alibaba.fastjson.JSON;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        Map<String, Object> responseData;
        responseData = new HashMap<>();
        if (exception instanceof UsernameNotFoundException) {
            responseData.put("status", 7001);
            responseData.put("msg", "用户不存在!");
        } else if (exception instanceof BadCredentialsException) {
            responseData.put("status", 7002);
            responseData.put("msg", "用户名或密码错误！");
        } else if (exception instanceof LockedException) {
            responseData.put("status", 7003);
            responseData.put("msg", "用户已被锁定！");
        } else if (exception instanceof DisabledException) {
            responseData.put("status", 7004);
            responseData.put("msg", "用户不可用！");
        } else if (exception instanceof AccountExpiredException) {
            responseData.put("status", 7005);
            responseData.put("msg", "账户已过期！");
        } else if (exception instanceof CredentialsExpiredException) {
            responseData.put("status", 7006);
            responseData.put("msg", "用户密码已过期！");
        } else {
            responseData.put("status", 7000);
            responseData.put("msg", "认证失败，请联系网站管理员！");
        }
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(200);
        response.getWriter().write(JSON.toJSONString(responseData));
    }
}
