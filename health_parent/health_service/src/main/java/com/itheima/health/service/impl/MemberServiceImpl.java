package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.pojo.Member;
import com.itheima.health.service.MemberService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/27 14:59
 */

@Service
public class MemberServiceImpl implements MemberService {


    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * 登录验证
     *
     * @Date 2020/9/27 14:59
     * @param: loginInfo
     **/
    @Override
    public void check(Map<String, String> loginInfo) {
        //判断用户是否是会员

        Member member = memberDao.findByTelephone(loginInfo.get("telephone"));
        if (member == null) {
            //注册
            member.setPhoneNumber(loginInfo.get("telephone"));
            member.setRegTime(new Date());
            member.setRemark("快速登陆");
            memberDao.addMember(member);
        }

    }

    /**
     * 获取会员统计
     *
     * @Date 2020/9/28 17:43
     * @param: months
     **/
    @Override
    public List<Integer> findMemberCountByDate(List<String> months) {

        List<Integer> memberCount = new ArrayList<>();
        if (months != null) {
            for (String month : months) {
                //拼接日 小于31
                Integer count = memberDao.findMemberCountByDate(month + "-31");
                memberCount.add(count);
            }
        }


        return memberCount;
    }

    /**
     * 运营数据统计
     *
     * @Date 2020/9/28 19:59
     * @param:
     **/
    @Override
    public Map<String, Object> getBusinessReportData() {
        // totalMember
        // thisWeekNewMember
        // thisMonthNewMember

        // todayOrderNumber
        // todayVisitsNumber
        // thisWeekOrderNumber
        // thisWeekVisitsNumber
        // thisMonthOrderNumber
        // thisMonthVisitsNumber
        // hotSetmeal
        Map<String, Object> map = new HashMap<>();
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date();
            //今天
            String date = sdf.format(today);
            //星期一
            String monday = sdf.format(DateUtils.getLastDayOfWeek(today));
            //星期日
            String sunday = sdf.format(DateUtils.getLastDayOfWeek(today));
            //每个月的第一天
            String oneday = sdf.format(DateUtils.getFirstDayOfThisMonth());
            //每个月的最后一天
            String lastDayOfMonth = sdf.format(DateUtils.getLastDayOfThisMonth());

            // todayNewMember

            //======会员=========
            int todayNewMember = memberDao.findMemberCountByToDay(date);
            int totalMember = memberDao.findAllMemberCount();
            int thisWeekNewMember = memberDao.findMemberCountByThisWeek(monday);
            int thisMonthNewMember = memberDao.findMemberCountByThiMonth(oneday);
            //==========订单============
            int todayOrderNumber = orderDao.findOrderCountByDate(date);
            int todayVisitsNumber=orderDao.findOrderVisitsNumberByDate(date);
            int thisWeekOrderNumber=orderDao.findOrderByThisWeek(monday,sunday);
            int thisWeekVisitsNumber=orderDao.findOrderVisitsNumberByThisWeek(monday);
            int thisMonthOrderNumber=orderDao.findOrderCountByMonth(oneday,lastDayOfMonth);
            int thisMonthVisitsNumber=orderDao.findOrderVisitsNumberByThisMonth(oneday);
            // reportDate
            map.put("reportDate", date);
            map.put("todayNewMember",todayNewMember);
            map.put("totalMember",totalMember);
            map.put("thisWeekNewMember",thisWeekNewMember);
            map.put("thisMonthNewMember",thisMonthNewMember);
            map.put("todayOrderNumber",todayOrderNumber);
            map.put("todayVisitsNumber",todayVisitsNumber);
            map.put("thisWeekOrderNumber",thisWeekOrderNumber);
            map.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
            map.put("thisMonthOrderNumber",thisMonthOrderNumber);
            map.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
            //===========热门套餐setmeal
            List<Map<String, Object>> hotSetmeal=orderDao.findHotSetmeal();

            map.put("hotSetmeal",hotSetmeal);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return map;

    }
}
