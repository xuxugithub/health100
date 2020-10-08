package com.itheima.health.service;

import com.itheima.health.controller.exception.HealthException;
import com.itheima.health.pojo.User;

import java.util.List;
import java.util.Map;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/26 20:44
 */
public interface UserService {


    /**登录验证
     * @Date 2020/9/26 20:45
     * @param: username
     **/
    User findByName(String username);

    /**查询用户的菜单信息
     * @Date 2020/9/30 12:11
     * @param: username
     **/
    List<Map<String, Object>> findAllMenu(String username);

}
