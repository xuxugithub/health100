package com.itheima.health.dao;

import com.itheima.health.pojo.User;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/26 20:47
 */
public interface UserDao {

    /**登录验证
     * @Date 2020/9/26 20:47
     * @param: username
     **/
    User findByName(String username);

}
