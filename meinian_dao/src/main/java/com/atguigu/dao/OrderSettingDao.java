package com.atguigu.dao;

import com.atguigu.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface OrderSettingDao {

//    添加预约设置
    void add(OrderSetting orderSetting);
//    查找指定预约日期是否设置了预约
    long findCountByOrderDate(String orderDate);
//    修改预约设置
    void editNumberByOrderDate(OrderSetting orderSetting);
//    查找指定预约日期的预约信息
    OrderSetting getOrderSettingByOrderDate(String orderDate);
    //获取所有预约设置数据
    List<OrderSetting> getOrderSettingsAll(@Param("dateBegin") String dateBegin,@Param("dataEnd") String dataEnd);

    void editNumberByDate(@Param("orderDate") String orderDate,@Param("number") Integer number);

    void editReservationsByOrderDate(OrderSetting orderSetting);

}
