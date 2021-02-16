package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.TravelGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/travelGroup")
public class TravelGroupController {

    @Reference
    private TravelGroupService travelGroupService;

    /**
     * 根据travelGroupId查询中间表的数据
     * @param travelGroupId
     * @return
     */
    @RequestMapping("/getTravelItemIds")
    public List<Integer> getTravelItemIds(Integer travelGroupId){
        List<Integer> travelItemIds = travelGroupService.getTravelItemIds(travelGroupId);
        return travelItemIds;
    }
    /**
     * 根据id查询跟团游
     * @param travelGroupId
     * @return
     */
    @RequestMapping("/getTravelGroupById")
    public TravelGroup getTravelGroupById(Integer travelGroupId){
        TravelGroup travelGroup = travelGroupService.getTravelGroupById(travelGroupId);
        return travelGroup;
    }

    /**
     * 删除跟团游
     * 判断是否存在关联的自由行，如果存在，返回false表示不能删除；如果不存在，调用删除方法，返回true表示删除成功
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        boolean flag = false;
        try {
            flag = travelGroupService.delete(id);

            if (flag){
                return new Result(true,MessageConstant.DELETE_TRAVELGROUP_SUCCESS);
            }
            return new Result(false,MessageConstant.DELETE_TRAVELGROUP_FAIL);

        } catch (Exception e) {
            return new Result(false,MessageConstant.DELETE_TRAVELGROUP_FAIL);
        }
    }

    /**
     * 添加跟团游
     *
     * @param travelItemIds
     * @param travelGroup
     * @return
     */
    @RequestMapping("/add")
    public Result add(Integer[] travelItemIds, @RequestBody TravelGroup travelGroup) {

        try {
            travelGroupService.add(travelItemIds, travelGroup);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_TRAVELGROUP_FAIL);
        }
        return new Result(true, MessageConstant.ADD_TRAVELGROUP_SUCCESS);
    }
    /**
     * 编辑跟团游
     *
     * @param travelItemIds
     * @param travelGroup
     * @return
     */
    @RequestMapping("/update")
    public Result update(Integer[] travelItemIds, @RequestBody TravelGroup travelGroup) {

        try {
            travelGroupService.update(travelItemIds, travelGroup);
        } catch (Exception e) {
            return new Result(false, MessageConstant.EDIT_TRAVELGROUP_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_TRAVELGROUP_SUCCESS);
    }

    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {

        PageResult pageResult = travelGroupService.findPage(queryPageBean);

        return pageResult;
    }

    /**
     * 查询所有跟团游信息
     * @return
     */
    @RequestMapping("/findAll")
    public List<TravelGroup> findAll(){
        List<TravelGroup> travelGroups = travelGroupService.findAll();
        return travelGroups;
    }
}
