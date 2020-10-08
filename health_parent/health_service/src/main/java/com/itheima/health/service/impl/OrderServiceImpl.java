package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.StringUtil;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.controller.exception.HealthException;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.itheima.health.pojo.Member;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/25 18:38
 */

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private MemberDao memberDao;

    @Override
    @Transactional
    public Order submit(Map<String, String> orderInfo) throws HealthException {
        //1.先去判断是否满足预约要求
        //(当天是否有预约 ,如果有再判断预约要求 最大预约数要大于已预约才能预约)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //前端获得电话
            String telephone = orderInfo.get("telephone");
            //时间
            String date = orderInfo.get("orderDate");
            //套餐id
            String setmealId = orderInfo.get("setmealId");
        //            throw new HealthException("日期格式转换失败");
        OrderSetting orderDate = null;
        Map<String, Object> map = null;
        Date orderDate1=null;
        try {
             orderDate1 = sdf.parse(date);
        } catch (ParseException e) {
            throw new HealthException("日期格式不正确，请选择正确的日期");
        }
        map = new HashMap<>();
        map.put("telephone", telephone);
        map.put("orderDate", orderDate1);
        map.put("setmealId", setmealId);
        orderDate = orderSettingDao.findByDate(orderDate1);

        if (orderDate == null) {

                throw new HealthException("当天没有预约安排,不能预约");
            }
        //判断最大预约数和已经预约数
            if (orderDate.getNumber() <= orderDate.getReservations()) {

                throw new HealthException("预约人数已满,请选择其他天");
            }
        //判断用户是否是会员
            Member member = memberDao.findByTelephone(telephone);
            if (member == null) {
                //会员不存在
                //添加会员
                member = new Member();
                // name 从前端来
                member.setName(orderInfo.get("name"));
                // sex  从前端来
                member.setSex(orderInfo.get("sex"));
                // idCard 从前端来
                member.setIdCard(orderInfo.get("idCard"));
                // phoneNumber 从前端来
                member.setPhoneNumber(telephone);
                // regTime 系统时间
                member.setRegTime(new Date());
                // password 可以不填，也可生成一个初始密码
                member.setPassword("12345678");
                // remark 自动注册
                member.setRemark("由预约而注册上来的");
                //   添加会员
                memberDao.addMember(member);
            }
            Order order = new Order();
            //会员存在
            //查看订单表 判断是否重复订购套餐
            List<Order> orderList = orderDao.findOrderByData(map);
            if (orderList.size() > 0) {
                throw new HealthException("你已经定过该套餐,请勿重复订购");
            }
            //订单不存在 ,添加订单
            order.setMemberId(Integer.valueOf(member.getId()));
            order.setOrderDate(orderDate1);
            order.setOrderType(MessageConstant.ORDERTYPE_WEI_XIN_);
            order.setSetmealId(Integer.valueOf(setmealId));
            order.setOrderStatus(MessageConstant.ORDERSTATUS_0_);
            orderDao.addOrder(order);

            //更改预约设置
            int affectByCount = orderSettingDao.editReservationsByOrderDate(orderInfo);
            if (affectByCount==0){
                throw new HealthException("更新预约失败");
            }
            return order;

    }
    /**预约成功
     * @Date 2020/9/25 21:56
     * @param: id
     **/
    @Override
    public Map<String, Object> findById(Integer id) {
        Map<String, Object> map= orderDao.findById(id);
        return map;
    }
}
