package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Reference
    private LoginService loginService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/login")
    public Result login(HttpServletResponse response, @RequestBody Map map){

        //验证手机号和验证码是否为空
        String telephone = (String) map.get("telephone");

        String validateCode = (String)map.get("validateCode");

        if (telephone==null || "".equals(telephone) || validateCode==null || "".equals(validateCode)){
            return new Result(false, MessageConstant.TELEPHONE_VALIDATECODE_NOTNULL);
        }

        String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);

        if (!validateCode.equals(code)){
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }

        loginService.login(telephone);
        //3：:登录成功
        //写入Cookie，跟踪用户
        Cookie cookie = new Cookie("login_member_telephone",telephone);
        cookie.setPath("/");//路径
        cookie.setMaxAge(60*60*24*30);//有效期30天（单位秒）
        response.addCookie(cookie);

        return new Result(true,MessageConstant.LOGIN_SUCCESS);

    }
}
