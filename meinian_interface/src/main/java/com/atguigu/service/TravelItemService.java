package com.atguigu.service;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.TravelItem;

import java.util.List;

public interface TravelItemService {
    //新增自由行
    void add(TravelItem travelItem);

    //分页查询自由行
    PageResult findPage(QueryPageBean queryPageBean);

    //删除自由行
    void delete(Integer id);
    //
    void edit(TravelItem travelItem);
    //查询所有自由行
    List<TravelItem> getItemsAll();
}
