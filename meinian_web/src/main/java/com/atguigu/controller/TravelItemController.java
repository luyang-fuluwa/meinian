package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import com.github.pagehelper.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/travelItem")
public class TravelItemController {

    @Reference
    private TravelItemService travelItemService;

    /**
     * 查询所有自由行信息
     * @return
     */
    @RequestMapping("/getItemsAll")
    public  List<TravelItem> getItemsAll(){

        List<TravelItem> list = travelItemService.getItemsAll();

        return list;
    }
    /**
     * 编辑自由行
     * @param travelItem
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody TravelItem travelItem){

        System.out.println("travelItem = " + travelItem);
        try {
            travelItemService.edit(travelItem);
        } catch (Exception e) {
            return new Result(false,MessageConstant.EDIT_TRAVELITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_TRAVELITEM_SUCCESS);
    }

    /**
     * 删除自由行
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            travelItemService.delete(id);
        } catch (Exception e) {
            return new Result(false,MessageConstant.DELETE_TRAVELITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_TRAVELITEM_SUCCESS);
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        PageResult page = travelItemService.findPage(queryPageBean);

        return page;
    }

    /**
     * 新增自由行
     * @param travelItem
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TravelItem travelItem){

        try {
            travelItemService.add(travelItem);
        } catch (Exception e) {
            return new Result(false,MessageConstant.ADD_TRAVELITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_TRAVELITEM_SUCCESS);
    }

}
