package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.util.SMSUtils;
import com.atguigu.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 预约时发送手机验证码
     * @return
     */
    @RequestMapping("/sendValidateCode")
    public Result sendValidateCode(String telephone){
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        try {
            SMSUtils.sendShortMessage(telephone,code.toString());
            //将验证码存到redis中
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 5 * 60, code.toString());

            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    /**
     * 登录时发送手机验证码
     * @param telephone
     * @return
     */
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        try {
            String code = ValidateCodeUtils.generateValidateCode4String(6);
            jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN,5*60,code);
            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (Exception e) {
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
