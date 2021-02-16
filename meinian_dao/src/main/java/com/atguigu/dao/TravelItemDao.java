package com.atguigu.dao;

import com.atguigu.pojo.TravelItem;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TravelItemDao {
    //新增自由行
    void add(TravelItem travelItem);


    //分页查询
    Page<TravelItem> findPage(@Param("queryString") String queryString);

    //删除自由行
    void delete(Integer id);

    //编辑自由行
    void edit(TravelItem travelItem);

    //查询所有自由行信息
    List<TravelItem> getItemsAll();


    List<TravelItem> getTravelItemsById(Integer id);


}
