package com.itheima.health.service;

import com.itheima.health.controller.exception.HealthException;
import com.itheima.health.pojo.User;

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


}
