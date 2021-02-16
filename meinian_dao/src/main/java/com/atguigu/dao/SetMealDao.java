package com.atguigu.dao;

import com.atguigu.pojo.Setmeal;
import com.atguigu.pojo.TravelGroup;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SetMealDao {


    void addSetmeal(Setmeal setmeal);


    void addSetmealAndTravelgroup(@Param("setmealId") Integer setmealId, @Param("travelGroupIds") Integer[] travelGroupIds);

    Page<Setmeal> findPage(@Param("queryString") String queryString);

    List<Setmeal> getSetmeal();

    Setmeal getSetmealById(Integer id);

    List<Map<String,Object>> getSetmealReport();

}
