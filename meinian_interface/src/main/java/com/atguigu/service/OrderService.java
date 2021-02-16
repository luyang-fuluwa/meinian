package com.atguigu.service;

import com.atguigu.entity.Result;

import java.util.Map;

public interface OrderService {

    //添加预约
    Result addOrder(Map map);

    Map findById(Integer id);
}
