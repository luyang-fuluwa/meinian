package com.atguigu.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelGroupDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.TravelGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = TravelGroupService.class)
@Transactional
public class TravelGroupServiceImpl implements TravelGroupService {

    @Autowired
    private TravelGroupDao travelGroupDao;

    /**
     * 分页查询
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {

        //开启分页插件
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        //调dao方法
        Page<TravelGroup> page = travelGroupDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 添加跟团游信息
     * @param travelGroup
     */
    @Override
    public void add(Integer[] travelItemIds, TravelGroup travelGroup) {
        //添加跟团游
        travelGroupDao.addTravelGroup(travelGroup);
        Integer travelGroupId = travelGroup.getId();
        //判断travelItemIds是否有值，如果有值添加中间表的数据
        if(travelItemIds != null && travelItemIds.length > 0){

            List<Integer> list = new ArrayList<>();
            for (Integer travelItemId : travelItemIds) {
                list.add(travelItemId);
            }
            travelGroupDao.addTravelGroupAndTravelItem(list,travelGroupId);
        }
    }

    /**
     * 删除跟团游
     * @param travelGroupId
     * @return
     */
    @Override
    public boolean delete(Integer travelGroupId) {
        //根据跟团游id查询中间表关联的自由行
        List<Integer> travelItemIds = travelGroupDao.getTravelGroupAndTravelItem(travelGroupId);

        //判断是否存在关联的自由行，如果存在，返回false表示不能删除；如果不存在，调用删除方法，返回true表示删除成功
        if (travelItemIds != null && travelItemIds.size() > 0){
            return false;
        }else {
            travelGroupDao.delete(travelGroupId);
            return true;
        }
    }

    /**
     * 通过id获取跟团游数据
     * @param travelGroupId
     * @return
     */
    @Override
    public TravelGroup getTravelGroupById(Integer travelGroupId) {
        TravelGroup travelGroup = travelGroupDao.getTravelGroupById(travelGroupId);
        return travelGroup;
    }

    /**
     * 根据travelGroupId查询中间表的数据
     * @param travelGroupId
     * @return
     */
    @Override
    public List<Integer> getTravelItemIds(Integer travelGroupId) {
        List<Integer> travelItemIds = travelGroupDao.getTravelGroupAndTravelItem(travelGroupId);
        return travelItemIds;
    }

    /**
     * 修改跟团游和中间表
     * @param travelItemIds
     * @param travelGroup
     */
    @Override
    public void update(Integer[] travelItemIds, TravelGroup travelGroup) {

        //根据travelGroupId删除中间表信息
        travelGroupDao.deleteTravelGroupAndTravelItem(travelGroup.getId());

        //根据travelGroupId修改跟团游信息
        travelGroupDao.updateTravelGroup(travelGroup);

        //添加中间表信息
        if(travelItemIds != null && travelItemIds.length > 0){

            List<Integer> list = new ArrayList<>();
            for (Integer travelItemId : travelItemIds) {
                list.add(travelItemId);
            }
            travelGroupDao.addTravelGroupAndTravelItem(list,travelGroup.getId());
        }

    }

    /**
     * 查询所有跟团游信息
     * @return
     */
    @Override
    public List<TravelGroup> findAll() {

        List<TravelGroup> travelGroups = travelGroupDao.findAll();

        return travelGroups;
    }
}
