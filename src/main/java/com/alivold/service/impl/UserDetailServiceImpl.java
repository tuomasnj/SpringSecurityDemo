package com.alivold.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alivold.config.BaseException;
import com.alivold.dao.SysUserMapper;
import com.alivold.domain.LoginUser;
import com.alivold.domain.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserMapper sysUserMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        SysUser user = null;
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getUserName, username);
        user = sysUserMapper.selectOne(lambdaQueryWrapper);
        if(ObjectUtil.isNull(user)){
            throw new RuntimeException("用户名或密码不存在");
        }
        return new LoginUser(user);
        // TODO: 2024/5/2 查询对应的用户权限信息
    }
}
