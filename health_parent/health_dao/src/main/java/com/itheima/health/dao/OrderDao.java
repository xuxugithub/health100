package com.itheima.health.dao;

import com.itheima.health.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/25 18:40
 */
public interface OrderDao {
   List<Order> findOrderByData(Map map);

    void addOrder(Order order);

    /**预约成功
     * @Date 2020/9/25 21:57
     * @param: id
     **/
    Map<String, Object> findById(Integer id);

    /**今日预约
     * @Date 2020/9/28 20:35
     * @param: date
     **/
    int findOrderCountByDate(String date);

    /**今日到诊数
     * @Date 2020/9/28 20:36
     * @param: date
     **/
    int findOrderVisitsNumberByDate(String date);

    /**本周预约数
     * @Date 2020/9/28 20:43
     * @param: monday
     * @param: sunday
     **/
    int findOrderByThisWeek(@Param("monday") String monday,@Param("sunday") String sunday);

    /**本周到诊数
     * @Date 2020/9/28 20:48
     * @param: monday
     **/
    int findOrderVisitsNumberByThisWeek(String monday);

    /**本月预约数
     * @Date 2020/9/28 20:55
     * @param: oneday
     * @param: lastDayOfMonth
     **/
    int findOrderCountByMonth(@Param("startDay") String startDay,@Param("endDay") String endDay);

    /**本月到诊数
     * @Date 2020/9/28 21:00
     * @param: oneday
     **/
    int findOrderVisitsNumberByThisMonth(String oneday);

    /**
     * @Date 2020/9/28 21:44
     * @param:
     **/
    List<Map<String, Object>> findHotSetmeal();


}
