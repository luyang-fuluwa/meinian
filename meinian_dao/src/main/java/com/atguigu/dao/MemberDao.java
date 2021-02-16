package com.atguigu.dao;

import com.atguigu.pojo.Member;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MemberDao {

//通过身份证号获取会员信息
    Member getMemberByIdCard(String idCard);
//通过手机号获取会员信息
    Member getMemberByTelephone(String telephone);

//添加会员
    void addMember(Member newMember);

    Integer getMemberReport(String month);

    public Integer findMemberCountBeforeDate(String date);
    List<Map<String, Object>> findSetmealCount();
    int getTodayNewMember(String date);
    int getTotalMember();
    int getThisWeekAndMonthNewMember(String date);


}
