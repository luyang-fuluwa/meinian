package com.atguigu.dao;

import com.atguigu.pojo.Address;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AddressDao {


    List<Address> findAllMaps();


    Page<Address> findPage(@Param("queryString") String queryString);

    void addAddress(Address address);

    void deleteById(Integer id);

}
