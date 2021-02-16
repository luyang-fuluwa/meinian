package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.RedisConstant;
import com.atguigu.dao.SetMealDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetMealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;


@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealDao setMealDao;

    @Autowired
    private JedisPool jedisPool;


    /**
     * 添加套餐游
     *
     * @param travelGroupIds
     * @param setmeal
     */
    @Override
    public void add(Integer[] travelGroupIds, Setmeal setmeal) {
//      1.添加套餐游的表
        setMealDao.addSetmeal(setmeal);
        Integer setmealId = setmeal.getId();

//      2.添加中间表
        if (travelGroupIds != null && travelGroupIds.length > 0) {
            setMealDao.addSetmealAndTravelgroup(setmealId, travelGroupIds);
        }

        //3.将图片名称保存到Redis
        String pic = setmeal.getImg();
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, pic);
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    public PageResult findPage(QueryPageBean queryPageBean) {

        //开启分页插件
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //调dao方法
        Page<Setmeal> page = setMealDao.findPage(queryPageBean.getQueryString());

        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 查询所有套餐游
     * @return
     */
    @Override
    public List<Setmeal> getSetmeal() {

        List<Setmeal> setmeals= setMealDao.getSetmeal();
        return setmeals;
    }

    /**
     * 查询套餐详情
     * @param id
     * @return
     */
    @Override
    public Setmeal getSetmealById(Integer id) {

        return setMealDao.getSetmealById(id);
    }

    @Override
    public List<Map<String,Object>> getSetmealReport() {

        return setMealDao.getSetmealReport();
    }

}
