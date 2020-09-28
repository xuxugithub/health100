package com.itheima.health.service;

import java.util.List;
import java.util.Map;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/27 14:44
 */
public interface MemberService {

    /**登录验证
     * @Date 2020/9/27 14:58
     * @param: loginInfo
     **/
    void check(Map<String, String> loginInfo);

    /**获取会员统计
     * @Date 2020/9/28 17:43
     * @param: months
     **/
    List<Integer> findMemberCountByDate(List<String> months);

    /**运营数据统计
     * @Date 2020/9/28 19:59
     * @param:
     **/
    Map<String, Object> getBusinessReportData();

}
