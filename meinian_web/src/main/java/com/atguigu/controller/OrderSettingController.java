package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderSettingService;
import com.atguigu.util.POIUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/orderSetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 文件上传
     * @param excelFile
     * @return
     */
    @RequestMapping("/upload")
    public Result upload(MultipartFile excelFile){

        try {
            // 使用poi工具类解析excel文件，读取里面的内容
            List<String[]> lists = POIUtils.readExcel(excelFile);
            // 把List<String[]> 数据转换成 List<OrderSetting>数据
            List<OrderSetting> orderSettings = new ArrayList<>();
            //  迭代里面的每一行数据，进行封装到集合里面
            for (String[] str : lists) {
                // 获取到一行里面，每个表格数据，进行封装
                OrderSetting orderSetting = new OrderSetting(str[0],Integer.parseInt(str[1]));
                orderSettings.add(orderSetting);
            }
            // 调用业务进行保存
            orderSettingService.add(orderSettings);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }

    }

    /**
     * 获取所有预约设置数据
     * @return
     */
    @RequestMapping("/getOrderSettingsAll")
    public Result getOrderSettingsAll(String dateStr){

        try {

            String dataBegin = dateStr+"-"+1;
            String dataEnd = dateStr+"-"+31;
            List<Map> list = new ArrayList<>();

            List<OrderSetting> allOrderSettings =  orderSettingService.getOrderSettingsAll(dataBegin,dataEnd);
            for (OrderSetting orderSetting : allOrderSettings) {

                String orderDate = orderSetting.getOrderDate();

                String date = orderDate.substring(orderDate.length()-2);

                int number = orderSetting.getNumber();

                int reservations = orderSetting.getReservations();

                HashMap<String, Object> map = new HashMap<>();

                map.put("date",date);
                map.put("number",number);
                map.put("reservations",reservations);

                list.add(map);
            }
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        }catch (Exception e){
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    /**
     * 修改预约人数
     * @param orderDate
     * @param number
     * @return
     */
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(String orderDate,Integer number){

        try {
            orderSettingService.editNumberByDate(orderDate,number);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }

}
