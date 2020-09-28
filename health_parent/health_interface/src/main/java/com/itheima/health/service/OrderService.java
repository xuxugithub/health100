package com.itheima.health.service;

import com.itheima.health.controller.exception.HealthException;
import com.itheima.health.pojo.Order;

import java.util.Map;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/25 18:34
 */
public interface OrderService {
    /**体检预约
     * @Date 2020/9/25 18:38
     * @param: orderInfo
     **/
    Order submit(Map<String, String> orderInfo) throws HealthException;

    /**预约成功
     * @Date 2020/9/25 21:56
     * @param: id
     **/
    Map<String, String> findById(Integer id);
}
