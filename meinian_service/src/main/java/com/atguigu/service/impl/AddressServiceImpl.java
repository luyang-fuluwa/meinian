package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.AddressDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.Address;
import com.atguigu.service.AddressService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = AddressService.class)
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressDao;

    /**
     * 查询地图位置
     * @return
     */
    @Override
    public Map<String, List> findAllMaps() {

        Map<String, List> map = new HashMap<>();

        List<Map<String,String>> gridnMaps = new ArrayList<>();

        List<Map<String,String>> nameMaps = new ArrayList<>();

        List<Address> allMaps = addressDao.findAllMaps();

        for (Address address : allMaps) {
            //遍历出一个address对象，获取addressName属性，放入map中，再把map放到list中
            String addressName = address.getAddressName();
            Map<String,String> addressNames = new HashMap<>();
            addressNames.put("addressName",addressName);
            nameMaps.add(addressNames);

            //遍历出一个address对象，获取lng和lat属性，放入map中，再把map放到list中
            String lng = address.getLng();
            String lat = address.getLat();
            Map<String,String> addressGrids = new HashMap<>();
            addressGrids.put("lng",lng);
            addressGrids.put("lat",lat);
            gridnMaps.add(addressGrids);

        }
        //将两个list放到返回的map中
        map.put("nameMaps",nameMaps);
        map.put("gridnMaps",gridnMaps);

        return map;
    }

    /**
     * 分页查询
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {

        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());

        Page page = addressDao.findPage(queryPageBean.getQueryString());

        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 添加
     */
    @Override
    public void addAddress(Address address) {
        addressDao.addAddress(address);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void deleteById(Integer id) {
        addressDao.deleteById(id);
    }


}
