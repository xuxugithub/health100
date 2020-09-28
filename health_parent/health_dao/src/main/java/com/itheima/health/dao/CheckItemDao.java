package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/18 17:12
 */
public interface CheckItemDao {

    List<CheckItem> findAll();


    Page<CheckItem> findByPage(String queryString);

    void add(CheckItem checkItem);

    void delete(int id);

    int findById(int id);

    CheckItem edit(int id);

    void update(CheckItem checkItem);

}
