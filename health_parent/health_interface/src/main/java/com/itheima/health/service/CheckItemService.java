package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.controller.exception.HealthException;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/18 17:04
 */
public interface CheckItemService {

    List<CheckItem> findAll();

    PageResult findByPage(QueryPageBean queryPageBean);

    void add(CheckItem checkItem);

    void delete(int id)throws HealthException;

    CheckItem editById(int id);

    void update(CheckItem checkItem);

}
