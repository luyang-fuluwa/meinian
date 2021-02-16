package com.atguigu.service;


import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.TravelGroup;

import java.util.List;
import java.util.Map;

public interface TravelGroupService {
    //分页查询
    PageResult findPage(QueryPageBean queryPageBean);


    //添加跟团游
    void add(Integer[] travelItemIds, TravelGroup travelGroup);

    //删除跟团游
    boolean delete(Integer travelGroupId);

    //通过id获取跟团游数据
    TravelGroup getTravelGroupById(Integer travelGroupId);

    //根据travelGroupId查询中间表的数据
    List<Integer> getTravelItemIds(Integer travelGroupId);

    //修改跟团游和中间表
    void update(Integer[] travelItemIds, TravelGroup travelGroup);

    //查询所有跟团游信息
    List<TravelGroup> findAll();
}
