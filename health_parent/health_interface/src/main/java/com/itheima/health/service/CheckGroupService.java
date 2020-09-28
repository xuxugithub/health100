package com.itheima.health.service;

import com.itheima.health.controller.exception.HealthException;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/19 22:20
 */
public interface CheckGroupService {

    void add(CheckGroup checkGroup, Integer[] ids);

    PageResult findPage(QueryPageBean queryPageBean);

    CheckGroup findById(int id);

    void update(CheckGroup checkGroup, Integer[] checkitemIds);

    void deleteById(int id)throws HealthException;

}
