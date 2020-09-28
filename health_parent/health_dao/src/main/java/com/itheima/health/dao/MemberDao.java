package com.itheima.health.dao;

import com.itheima.health.pojo.Member;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/25 18:40
 */
public interface MemberDao {

    /**通过电话查询 会员
     * @Date 2020/9/25 19:31
     * @param: telephone
     **/
    Member findByTelephone(String telephone);

       /**添加会员
        * @Date 2020/9/27 15:13
        * @param: member
        **/
    void addMember(Member member);

    /**获取会员统计
     * @Date 2020/9/28 17:44
     * @param: months
     **/
    Integer findMemberCountByDate(String month);

    /**会员总数
     * @Date 2020/9/28 20:16
     * @param:
     **/
    int findAllMemberCount();

    /**今日新增
     * @Date 2020/9/28 20:16
     * @param: today
     **/
    int findMemberCountByToDay(String today);

    /**本周新增
     * @Date 2020/9/28 20:18
     * @param: monday
     **/
    int findMemberCountByThisWeek(String monday);

    /**本月新增会员
     * @Date 2020/9/28 20:28
     * @param: oneday
     **/
    int findMemberCountByThiMonth(String oneday);
}
