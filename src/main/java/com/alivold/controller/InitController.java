package com.alivold.controller;

import com.alivold.dao.UserMapper;
import com.alivold.domain.User;
import com.alivold.resp.ResponseResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("/init")
public class InitController {
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('sys:save')")
    public ResponseResult handleHello(){
//        User user = new User();
//        user.setUsername("托马斯穆勒");
//        user.setPassword("888999123");
//        user.setEmail("123@123.com");
//        user.setCreateTime(new Date());
//        userMapper.insert(user);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", "托马斯穆勒");
        return ResponseResult.success(userMapper.selectList(queryWrapper));
    }
}
