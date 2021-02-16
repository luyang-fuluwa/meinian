package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetMealService;
import com.atguigu.util.SMSUtils;
import com.atguigu.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {

    @Reference
    private SetMealService setMealService;

    /**
     * 查询所有套餐游
     * @return
     */
    @RequestMapping("/getSetmeal")
    public Result getSetmeal(){

        List<Setmeal> setmeals = null;
        try {
            setmeals = setMealService.getSetmeal();

            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,setmeals);

        } catch (Exception e) {
            return new Result(false,MessageConstant.GET_SETMEAL_LIST_FAIL);

        }

    }

    /**
     * 查询套餐详情
     * @param id
     * @return
     */
    @RequestMapping("/getSetmealById")
    public Result getSetmealById(Integer id){

        Setmeal setmeal = setMealService.getSetmealById(id);

        return new Result(true,"查询套餐详情成功",setmeal);
    }
}
