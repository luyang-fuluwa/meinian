package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.MemberDao;
import com.atguigu.pojo.Member;
import com.atguigu.service.LoginService;
import com.atguigu.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service(interfaceClass = LoginService.class)
@Transactional
public class LoginServiceImpl implements LoginService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public void login(String telephone) {

        Member member = memberDao.getMemberByTelephone(telephone);

        if (member!=null){
            return;
        }

        Member newMember = new Member();

        newMember.setPhoneNumber(telephone);
        try {
            newMember.setRegTime(DateUtils.parseDate2String(new Date()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        memberDao.addMember(newMember);
    }
}
