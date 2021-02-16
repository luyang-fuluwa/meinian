package com.atguigu.service;

import com.atguigu.pojo.OrderSetting;

import java.util.List;

public interface OrderSettingService {


    void add(List<OrderSetting> orderSettings);

    List<OrderSetting> getOrderSettingsAll(String dateBegin,String dataEnd);

    void editNumberByDate(String orderDate, Integer number);
}
