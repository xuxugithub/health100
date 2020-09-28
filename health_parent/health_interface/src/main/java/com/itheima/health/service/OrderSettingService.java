package com.itheima.health.service;

import com.itheima.health.controller.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/22 18:47
 */
public interface OrderSettingService {
    void upload(List<OrderSetting> orderSettingList)throws HealthException;

    /**日历展示预约信息
     * @Date 2020/9/22 20:35
     * @param: excelFile
     **/
    List<Map<String, Integer>> getOrderSettingMonth(String month);
    /**更改预约设置信息
     * @Date 2020/9/22 21:20
     * @param: orderSetting
     **/
    void updateNumber(OrderSetting orderSetting)throws HealthException;

}
