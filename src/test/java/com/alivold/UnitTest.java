package com.alivold;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alivold.dao.SysUserMapper;
import com.alivold.dao.UserMapper;
import com.alivold.domain.SysUser;
import com.alivold.domain.User;
import com.alivold.util.JwtUtil;
import com.alivold.util.LoginUserInfoUtil;
import com.alivold.util.RedisCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class UnitTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    @Qualifier("passwordEncoder")
    private PasswordEncoder passwordEncoder;

    @Autowired
    RedisCache redisCache;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Test
    public void testMapper(){
        List<User> users = userMapper.selectAllUsers();
        for (User u: users) {
            log.info(u.toString());
        }
    }

    @Test
    public void objToJson(){
        User user = new User(12, "铁蛋", "654321", "124447@cn.com", Calendar.getInstance().getTime());
        System.out.println(Calendar.getInstance().getTime());
        String s = JSON.toJSON(user).toString();
        log.info("Json:{}", s);
        JSONObject o = JSON.parseObject(s);
        log.info("解析json======{}", o.getInteger("id"));
    }

    @Test
    public void testRedis1(){
        redisTemplate.opsForValue().set("user_info_1", new User(0, "1号", "444555", "123@qq.com", new Date()));
        User userInfo1 = (User)redisTemplate.opsForValue().get("user_info_1");
        String s= JSON.toJSONString(userInfo1);
        JSONObject jsonObject = JSON.parseObject(s);
        log.info("jsonObject,{}", jsonObject);
    }

    @Test
    public void testRedis2(){
        redisCache.setCacheObject("user_info_test_1", new User(0, "1号", "444555", "123@qq.com", new Date()), 30);
        User user = redisCache.getCacheObject("user_info_test_1");
        log.info("user{}", user);

        ArrayList<User> users = new ArrayList<>();
        users.add(new User(0, "1号", "444555", "123@qq.com", new Date()));
        users.add(new User(1, "2号", "144556688", "77773@qq.com", new Date()));
        redisCache.setCacheList("list_1", users);
        List<User> list = redisCache.getCacheList("list_1");
        for (int i = 0; i < list.size(); i++) {
            log.info(list.get(i).toString());
        }
        redisCache.expire("list_1", 60);
    }

    @Test
    public void testMap(){
        Map<String, String> map = new HashMap<>();
        System.out.println(map == null);
        System.out.println(map.isEmpty());
    }

    @Test
    public void testPasswordEncoder(){
        // System.out.println(passwordEncoder.encode("66666"));
        System.out.println(passwordEncoder.matches("888888", "$2a$10$1CwXFLv94XWRPq7.E8/Npe0yVxEbRQKXR/4DifP9izQRJ6Jcu8VTe"));
        System.out.println(passwordEncoder.matches("666666", "$2a$10$ww9568ERH5BIkfuZE9AyEek7Ueppd4J8olti4YCMEFoDORBHEw4nu"));
    }

    @Test
    public void testJwt() throws Exception {
        SysUser user = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUserName, "user2"));
        System.out.println(user);
        //实体类转jsonstr
        String str = JSON.toJSONString(user);
        System.out.println("JSONstr"+ str);
        //根据jsonstr值创建jwt
        String sysUserJwt = JwtUtil.createJwt(str);
        log.info("sysUserJwt========={}", sysUserJwt);
        //从jwt中获取jsonstr
        String json = (String)JwtUtil.parseJwt(sysUserJwt).get("sub");

        //解析jsonstr
        SysUser user1 = JSONObject.parseObject(json, SysUser.class);
        log.info("{}",user1);
    }

    @Test
    public void insertSysuser(){
        SysUser u = new SysUser();
        u.setUserName("uuuu");
        u.setPassword("5689999");
        u.setNickName("阿旺");
        sysUserMapper.insert(u);
    }

    @Test
    public void testJwtTime() throws Exception {
        String pwd = "pass";
        String jwt = JwtUtil.createJwt(pwd);
        System.out.println(jwt);
        Claims claims = JwtUtil.parseJwt(jwt);
        System.out.println(claims.getSubject());
    }
}
