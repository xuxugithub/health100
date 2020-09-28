package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/20 19:13
 */
public interface SetmealDao {

    Page<Setmeal> findPage(String queryString);

    List<CheckGroup> findAll();

    void add(Setmeal setmeal);

    void addById(@Param("setmealId")Integer setmealId, @Param("checkgroupId")Integer checkgroupId);
    //回显套餐信息
    Setmeal findSetmealById(Integer id);

    List<Integer> findAllCheckedId(Integer id);

    void update(Setmeal setmeal);

    void deleteSetMealCheckGroupById(Integer id);

    int findBySetmealId(Integer id);

    void deleteSetMealById(Integer id);
     /**查找所有图片
      * @Date  17:43
      * @param:
      **/
    List<String> findAllImgs();
    /**套餐展示
     * @Date 2020/9/23 17:59
     * @param:
     **/
    List<Setmeal> findAllSetmeal();
    /**套餐详情展示
     * @Date 2020/9/23 19:34
     * @param: id
     **/
    Setmeal findDetailById(Integer id);
    /**套餐详情展示2
     * @Date 2020/9/23 19:34
     * @param: id
     **/
    Setmeal findDetailById2(Integer id);

    Setmeal findById(Integer id);

    List<Map<String, Object>> findSetmealCount();


}
