package com.atguigu.dao;

import com.atguigu.pojo.TravelGroup;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TravelGroupDao {

    //分页查询
    Page<TravelGroup> findPage(@Param("queryString") String queryString);

    //添加跟团游
    void addTravelGroup(TravelGroup travelGroup);

    //添加中间表信息
    void addTravelGroupAndTravelItem(@Param("list") List<Integer> list, @Param("travelGroupId") Integer travelGroupId);

    //根据跟团游id查询中间表关联的自由行
    List<Integer> getTravelGroupAndTravelItem(Integer travelgroupId);

    // 删除跟团游
    void delete(Integer travelGroupId);

    //根据id获取跟团游信息
    TravelGroup getTravelGroupById(Integer travelGroupId);

    //根据travelGroupId删除中间表信息
    void deleteTravelGroupAndTravelItem(Integer travelGroupId);

    //修改跟团游信息
    void updateTravelGroup(TravelGroup travelGroup);

    //查询所有跟团游信息
    List<TravelGroup> findAll();

    List<TravelGroup> getTravelGroupsById(Integer id);


}
