package com.alivold.authentication;
import com.alivold.dao.SysUserMapper;
import com.alivold.domain.LoginUser;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import com.alivold.domain.SysUser;
import com.mysql.cj.conf.PropertySet;
import com.mysql.cj.exceptions.ExceptionInterceptor;
import com.mysql.cj.protocol.Protocol;
import com.mysql.cj.protocol.ServerSession;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public Authentication authenticate(Authentication authentication) {
        // 从 authentication 中获取手机号和验证码
        //SmsCodeAuthenticationToken是Authentication接口的实现类
        String phoneNumber = (String) authentication.getPrincipal();
        String smsCode = (String) authentication.getCredentials();

        boolean isValid = verifySmsCode(phoneNumber, smsCode);

        if (isValid) {
            // 创建 UserDetails 实例
            UserDetails userDetails = loadUserByPhoneNumber(phoneNumber);
         //   return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
            return new SmsCodeAuthenticationToken(userDetails, smsCode, null); //暂时先不返回用户权限
        } else {
            // 验证码错误或过期，抛出异常
            throw new BadCredentialsException("验证码错误");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private boolean verifySmsCode(String phoneNumber, String smsCode) {
        // 实现验证码的验证逻辑
        if(phoneNumber.equals("19850097302") && smsCode.equals("980628")){
            return true;
        }
        return false;
    }

    private UserDetails loadUserByPhoneNumber(String phoneNumber) {
        // 实现根据手机号加载用户信息的逻辑
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<SysUser>();
        lambdaQueryWrapper.eq(SysUser::getPhonenumber, phoneNumber);
        SysUser sysUser = sysUserMapper.selectOne(lambdaQueryWrapper);
        return new LoginUser(sysUser);
    }
}