package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.MessageConstant;
import com.atguigu.dao.MemberDao;
import com.atguigu.dao.OrderDao;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.pojo.Order;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    /**
     * 添加预约
     * @param map
     */
    @Override
    public Result addOrder(Map map) {

        //将map拆箱
        String name = (String) map.get("name");
        String sex = (String) map.get("sex");
        String phoneNumber = (String) map.get("telephone");
        String idCard = (String) map.get("idCard");
        String orderDate = (String) map.get("orderDate");
        String orderType = Order.ORDERTYPE_WEIXIN;
        String orderStatus = Order.ORDERSTATUS_NO;
        Integer setmealId = (Integer) map.get("setmealId");

        /*
            1.判断当前用户是否是会员
                如果是会员则直接进行预约操作，
                如果不是，则先注册会员，返回一个memberId

            2.判断能否预约:两种情况
               （1）预约设置表中没有对应日期的数据
               （2）预约人数已满
            3.判断用户是否重复预约，（同一个用户同一天预约了同一个套餐）。重复则不能预约

            4.预约成功，更新当日已预约人数
         */

        //1.验证是否注册会员，如果没注册，就注册一个，返回memberId
        Member member = memberDao.getMemberByIdCard(idCard);
        Integer memberId = null;

        if (member==null){
            Member newMember = new Member();
            newMember.setName(name);
            newMember.setSex(sex);
            newMember.setIdCard(idCard);
            newMember.setPhoneNumber(phoneNumber);
            newMember.setRegTime(orderDate);
            memberDao.addMember(newMember);
            memberId = newMember.getId();
        }else {
            //取得会员id
            memberId= member.getId();
        }

        //2.判断能否预约:两种情况
        OrderSetting orderSetting = orderSettingDao.getOrderSettingByOrderDate(orderDate);
        //1）判断该日是否进行了预约设置
        if (orderSetting==null){
            return new Result(false, MessageConstant.ORDER_FAIL);
        }
        //2）判断预约人数是否已满
        if (orderSetting.getNumber()==orderSetting.getReservations()){
            return new Result(false,MessageConstant.ORDER_FULL);
        }

        //3.判断用户是否重复预约，（同一个用户同一天预约了同一个套餐）。重复则不能预约
        Order order = orderDao.getOrder(memberId,orderDate,setmealId);

        if (order != null){
            return new Result(false,MessageConstant.HAS_ORDERED);
        }

        //4.预约成功，更新当日已预约人数

        //封装成bean对象
        order = new Order(memberId,orderDate,orderType,orderStatus,setmealId);

        orderDao.addOrder(order);

        Integer id = order.getId();

        orderSetting.setReservations(orderSetting.getReservations() + 1);
        //修改当日已预约人数
        orderSettingDao.editReservationsByOrderDate(orderSetting);


        return new Result(true,MessageConstant.ORDER_SUCCESS,id);

    }

    /**
     * 根据预约id查询信息
     * @param id
     * @return
     */
    @Override
    public Map findById(Integer id) {

        Map map = orderDao.findById(id);

        return map;
    }
}
