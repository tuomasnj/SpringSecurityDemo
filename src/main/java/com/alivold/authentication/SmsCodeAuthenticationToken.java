package com.alivold.authentication;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @description : 自定义一个认证token，这里是手机号和手机验证码；也可以是用户名、用户密码和图形验证码
 * @author : guomingxian
 * @Date : 2024/5/7 0:22
 */
public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 1L;

    // 存储用户的手机号码
    private Object principal;

    // 存储短信验证码
    private Object credentials;

    public SmsCodeAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    public SmsCodeAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    /**
     * 获取手机号
     * @return
     */
    @Override
    public Object getCredentials() {
        return this.credentials;
    }


    /**
     * 获取验证码
     * @return
     */
    @Override
    public Object getPrincipal() {
        return this.principal;
    }


    /**
     * 设置认证状态。
     *
     * @param authenticated 认证状态
     * @throws IllegalArgumentException 如果传入的认证状态是null
     */
    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        super.setAuthenticated(authenticated);
    }

    /**
     * 获取用户的权限集合。
     *
     * @return 权限集合
     */
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }
}