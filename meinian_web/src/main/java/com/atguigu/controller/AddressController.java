package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Address;
import com.atguigu.service.AddressService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Reference
    private AddressService addressService;

    @RequestMapping("/findAllMaps")
    public Map findAllMaps(){

        Map<String, List> allMaps = addressService.findAllMaps();

        return allMaps;
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        PageResult pageResult = addressService.findPage(queryPageBean);
        return pageResult;
    }

    /**
     * 添加
     * @param address
     * @return
     */
    @RequestMapping("/addAddress")
    public Result addAddress(@RequestBody Address address){

        try {
            addressService.addAddress(address);

            return new Result(true,"添加成功");
        } catch (Exception e) {
            return new Result(false,"添加失败");
        }
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @RequestMapping("/deleteById")
    public Result deleteById(Integer id){
        try {
            addressService.deleteById(id);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            return new Result(false,"删除失败");
        }

    }

}
