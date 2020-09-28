package com.itheima.health.service;

import com.itheima.health.controller.exception.HealthException;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/20 19:12
 */
public interface SetmealServicce {
    PageResult findPage(QueryPageBean queryPageBean);

    List<CheckGroup> findAll();

    Integer add(Setmeal setmeal, Integer[] checkgroupIds);

    Map<String, Object> findSetmealById(Integer id);

    List<Integer> findAllCheckedId(Integer id);

    void update(Setmeal setmeal, Integer[] checkgroupIds);

    void delete(Integer id)throws HealthException;

    List<String> findAllImgs();

    /**套餐列表展示
     * @Date 2020/9/23 17:57
     * @param:
     **/
    List<Setmeal> findAllSetmeal();
    /**套餐详情列表
     * @Date 2020/9/23 18:30
     * @param: id
     **/
    Setmeal findDetailById(Integer id);
    /**套餐详情列表2
     * @Date 2020/9/23 18:30
     * @param: id
     **/
    Setmeal findDetailById2(Integer id);

    Setmeal findById(Integer id);

    /**获取套餐预约占比
     * @Date 2020/9/28 18:23
     * @param:
     **/
    List<Map<String, Object>> findSetmealCount();

}
