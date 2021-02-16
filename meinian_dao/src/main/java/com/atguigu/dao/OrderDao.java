package com.atguigu.dao;

import com.atguigu.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderDao {

//根据3个条件判断用户是否重复预约
    Order getOrder(@Param("memberId") Integer memberId,@Param("orderDate") String orderDate,@Param("setmealId") Integer setmealId);

    void addOrder(Order order);

    Map findById(Integer id);

    int getTodayOrderNumber(String date);
    int getTodayVisitsNumber(String date);
    int getThisWeekAndMonthOrderNumber(Map<String, Object> map);
    int getThisWeekAndMonthVisitsNumber(Map<String, Object> map);
    List<Map<String,Object>> findHotSetmeal();

}
