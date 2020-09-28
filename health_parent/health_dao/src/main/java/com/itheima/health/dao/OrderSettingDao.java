package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/22 19:37
 */
public interface OrderSettingDao {


    OrderSetting findByDate(Date orderDate);

    void updateByNumber(OrderSetting orderSetting);

    void add(OrderSetting orderSetting);

    List<Map<String, Integer>> getOrderSettingMonth(@Param("startDate") String startDate,@Param("endDate")  String endDate);

    /**更改预约
     * @Date 2020/9/25 20:25
     * @param: orderInfo
     **/
    int editReservationsByOrderDate(Map<String, String> orderInfo);

}
