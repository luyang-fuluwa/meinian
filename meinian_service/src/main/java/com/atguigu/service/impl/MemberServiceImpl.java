package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.MemberDao;
import com.atguigu.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public List<Integer> getMemberReport(List<String> months) {

        List<Integer > list = new ArrayList<>();

        for (String month : months) {

            Integer memberCount = memberDao.getMemberReport(month + "-31");
            list.add(memberCount);
        }
        return list;
    }
}
