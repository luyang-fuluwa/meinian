package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/orderMobile")
public class OrderMobileController {

    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;

    /**
     * 添加预约
     * @param map
     * @return
     */
    @RequestMapping("/addOrder")
    public Result addOrder(@RequestBody Map map){

        try {
            //1.校验手机验证码
            String validateCode = (String) map.get("validateCode");

            String telephone = (String) map.get("telephone");

            if (validateCode==null || "".equals(validateCode) || telephone==null || "".equals(telephone)){
                return new Result(false, MessageConstant.TELEPHONE_VALIDATECODE_NOTNULL);
            }

            String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);

            if (!validateCode.equals(code)){
                return new Result(false,MessageConstant.VALIDATECODE_ERROR);
            }

            //调用业务层方法
            Result result = orderService.addOrder(map);

            return result;

        } catch (Exception e) {

            return new Result(false,MessageConstant.ORDER_FAIL);

        }

    }

    /**
     * 根据预约id查询信息
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
