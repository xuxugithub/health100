package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.controller.exception.HealthException;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/22 19:36
 */
@Service
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;


    /**批量预约
     * @Date 2020/9/22 20:01
     * @param: orderSettingList
     **/
    @Transactional
    @Override
    public void upload(List<OrderSetting> orderSettingList)throws HealthException {
        //通过时间查询是否有
        for (OrderSetting orderSetting : orderSettingList) {
           OrderSetting orderIndb=orderSettingDao.findByDate(orderSetting.getOrderDate());
           if (orderIndb!=null){
            //存在判断  已预约人数是否大于最大预约人数
               //大于则抛异常
               if (orderSetting.getNumber()<orderIndb.getReservations()){
                    throw  new HealthException("最大预约数量不能小于已预约数量");

               }
                   //小于则修改最多预约
                   orderSettingDao.updateByNumber(orderSetting);


           }else
           {

               //没有就插入
               orderSettingDao.add(orderSetting);
           }
        }

    }

    /**日历展示预约信息
     * @Date 2020/9/22 20:47
     * @param: month
     **/
    @Override
    public List<Map<String, Integer>> getOrderSettingMonth(String month) {
        //拼接查询条件
        String startDate=month+"-01";
        String endDate=month+"-31";

        List<Map<String, Integer>> list=  orderSettingDao.getOrderSettingMonth(startDate,endDate);
        return list;

    }
    /**更改预约设置信息
     * @Date 2020/9/22 21:20
     * @param: orderSetting
     **/
    @Override
    public void updateNumber(OrderSetting orderSetting)throws HealthException {
            //通过日期判断预约设置否是存在
        OrderSetting orderIndb=orderSettingDao.findByDate(orderSetting.getOrderDate());
        if (orderIndb!=null){
            //存在判断  已预约人数是否大于最大预约人数
            //大于则抛异常
            if (orderSetting.getNumber()<orderIndb.getReservations()){
                throw  new HealthException("最大预约数量不能小于已预约数量");

            }
                //小于则修改最多预约
                orderSettingDao.updateByNumber(orderSetting);


        }else {
            orderSettingDao.add(orderSetting);
        }
        //没有就插入


    }


}
