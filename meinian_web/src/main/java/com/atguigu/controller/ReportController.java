package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.service.MemberService;
import com.atguigu.service.ReportService;
import com.atguigu.service.SetMealService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.loading.PrivateClassLoader;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetMealService setMealService;

    @Reference
    private ReportService reportService;

    /**
     * 获取会员统计
     *跟据图表模型，后端需要返回两个list集合
     * 所以将两个list封装进map
     * 在前台生成折线图
     * @return
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){

        Map<String,Object> map = new HashMap<>();

        List<String> months = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.MONTH,-12);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");

        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH,1);
            months.add(simpleDateFormat.format(calendar.getTime()));
        }

        //把moths集合放入map
        map.put("months",months);

        List<Integer> memberCount = null;
        try {
            memberCount = memberService.getMemberReport(months);
        } catch (Exception e) {
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
        //把memberCount放入map
        map.put("memberCount",memberCount);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }

    /**
     * 获取套餐游统计
     * 将数据返回
     * 在前台生成饼状图
     * @return
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){

        try {
            List<Map<String, Object>> list = setMealService.getSetmealReport();

            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,list);
        } catch (Exception e) {
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    /**
     * 统计数据
     * @return
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {

            Map<String,Object> map = reportService.getBusinessReportData();

            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);

        } catch (Exception e) {

            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }

    }

    /**
     * 统计数据下载
     */
    @RequestMapping("/exportBusinessReport")
    private void exportBusinessReport(HttpServletRequest request, HttpServletResponse response){

        try {
            //1.获取数据
            Map<String,Object> result = reportService.getBusinessReportData();

            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //2.获取模板文件真是路径——>输入流读取文件——>创建文件——>选择工作簿

            //获取文件所在的真实路径
            String path = request.getSession().getServletContext().getRealPath("template")+ File.separator+"report_template.xlsx";
            //创建一个输入流，通过真实路径，去读取模板文件
            FileInputStream fileInputStream = new FileInputStream(path);
            //通过POI创建一个模板文件样子的文件
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            //选择文件中的第一张表
            XSSFSheet sheet = workbook.getSheetAt(0);


            //3.对应文件单元格写入数据
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期


            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日出游数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周出游数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月出游数

            int rowNum = 12;
            for(Map map : hotSetmeal){//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }


            //4.以流的形式响应回浏览器
            ServletOutputStream out = response.getOutputStream();
            // 下载的数据类型（excel类型）
            response.setContentType("application/vnd.ms-excel");
            // 设置下载形式(通过附件的形式下载)
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(out);

            //关流
            out.flush();
            out.close();
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
