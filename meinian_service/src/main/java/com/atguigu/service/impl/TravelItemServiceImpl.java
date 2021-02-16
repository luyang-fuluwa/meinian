package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelItemDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = TravelItemService.class)
@Transactional
public class TravelItemServiceImpl implements TravelItemService {

    @Autowired
    private TravelItemDao travelItemDao;

    /**
     * 新增自由行
     * @param travelItem
     */
    @Override
    public void add(TravelItem travelItem) {

        travelItemDao.add(travelItem);
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //开启分页插件功能
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<TravelItem> page = travelItemDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 删除自由行
     * @param id
     */
    @Override
    public void delete(Integer id) {
        travelItemDao.delete(id);
    }

    /**
     * 编辑自由行
     * @param travelItem
     */
    @Override
    public void edit(TravelItem travelItem) {
        travelItemDao.edit(travelItem);
    }

    /**
     * 查询所有自由行信息
     * @return
     */
    @Override
    public List<TravelItem> getItemsAll() {
        List<TravelItem> list = travelItemDao.getItemsAll();
        return list;
    }
}
