
package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.controller.exception.HealthException;
import com.itheima.health.dao.UserDao;
import com.itheima.health.entity.RoleMenu;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/26 20:46
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findByName(String username) {
        return userDao.findByName(username);
    }

    /**
     * 查询用户的菜单信息
     *
     * @Date 2020/9/30 12:12
     * @param: username
     **/
    @Override
    public List<Map<String, Object>> findAllMenu(String username) {

        List<Map<String, Object>> menuMapList = userDao.findAllMenu(username);

        for (Map<String, Object> map : menuMapList) {
            List<Map<String, Object>> mapList = userDao.findAllSonMenu(username,(Integer)map.get("id"));
            map.put("children",mapList);
        }

        return menuMapList;
    }
}