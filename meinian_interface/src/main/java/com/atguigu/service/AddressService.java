package com.atguigu.service;

import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.Address;

import java.util.List;
import java.util.Map;

public interface AddressService {
    Map<String, List> findAllMaps();

    PageResult findPage(QueryPageBean queryPageBean);


    void addAddress(Address address);

    void deleteById(Integer id);
}
