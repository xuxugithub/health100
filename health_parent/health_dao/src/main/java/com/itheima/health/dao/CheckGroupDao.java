package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/19 22:24
 */
public interface CheckGroupDao {

    void add(CheckGroup checkGroup);

    void addById(@Param("checkGroupId") Integer checkGroupId, @Param("checkitemId") Integer checkitemId);

    Page<CheckGroup> findPage(String queryString);

    CheckGroup findById(int id);

    List<CheckItem> findByCheckGroupId(int id);

    void update(CheckGroup checkGroup);

    void updateById(@Param("id") Integer id, @Param("checkitemId") Integer checkitemId);

    int findByIdFromSC(int id);
    //删除旧关系
    void deleteById(int id);

    void deleteFromCheckGroup(int id);


}