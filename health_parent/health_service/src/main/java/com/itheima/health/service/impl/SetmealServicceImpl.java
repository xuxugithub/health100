package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.itheima.health.controller.exception.HealthException;

import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealServicce;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/20 19:12
 */
@Service
public class SetmealServicceImpl implements SetmealServicce {

    @Autowired
    private SetmealDao setmealDao;

    /**
     * 分页查询套餐
     *
     * @Date 2020/9/20 19:29
     * @param: queryPageBean
     **/
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //使用分页工具
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        if (!StringUtil.isEmpty(queryPageBean.getQueryString())) {
            //如果查询条件不为空 就拼接"%"
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }

        Page<Setmeal> page = setmealDao.findPage(queryPageBean.getQueryString());

        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 查询所有的检查组
     *
     * @Date 2020/9/20 22:03
     * @param:
     **/
    @Override
    public List<CheckGroup> findAll() {

        List<CheckGroup> checkGroupList = setmealDao.findAll();

        return checkGroupList;
    }

    /**
     * 新增套餐
     *
     * @Date 2020/9/20 22:03
     * @param: setmeal
     * @param: checkgroupIds
     **/
    @Override
    @Transactional
    public Integer add(Setmeal setmeal, Integer[] checkgroupIds) {
        //先增加主体 套餐
        setmealDao.add(setmeal);
        //返回主键id
        Integer setmealId = setmeal.getId();
        //再添加关系表
        if (checkgroupIds != null) {
            for (Integer checkgroupId : checkgroupIds) {

                setmealDao.addById(setmealId, checkgroupId);

            }
        }
        return setmealId;
    }

    /**
     * 回显套餐数据
     *
     * @Date 2020/9/21 19:32
     * @param: id
     **/
    @Override
    public Map<String, Object> findSetmealById(Integer id) {

        Setmeal setmeal = setmealDao.findSetmealById(id);

        String domain = QiNiuUtils.DOMAIN;
        Map<String, Object> map = new HashMap<>();
        map.put("setmeal", setmeal);
        map.put("domain", domain);
        return map;
    }

    /**
     * 获取被选中的检查组id
     *
     * @Date 2020/9/21 19:50
     * @param: id
     **/
    @Override
    public List<Integer> findAllCheckedId(Integer id) {
        List<Integer> list = setmealDao.findAllCheckedId(id);
        return list;
    }

    /**
     * 修改套餐
     *
     * @Date 2020/9/21 20:16
     * @param: setmeal
     * @param: checkgroupIds
     **/
    @Override
    @Transactional
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {
        //先修改套餐
        setmealDao.update(setmeal);
        //删除旧关系
        setmealDao.deleteSetMealCheckGroupById(setmeal.getId());
        //添加新关系
        if (checkgroupIds != null) {
            for (Integer checkgroupId : checkgroupIds) {
                //插入关系表
                setmealDao.addById(setmeal.getId(), checkgroupId);
            }

        }

    }

    /**
     * 删除套餐
     *
     * @Date 2020/9/21 21:38
     * @param: id
     **/
    @Override
    @Transactional
    public void delete(Integer id) throws HealthException {
        //先去订单中查询是否有该套餐id
        int cnt = setmealDao.findBySetmealId(id);
        //如果有,则抛异常
        if (cnt > 0) {
            throw new HealthException("该套餐已被使用,不能删除");
        }
        //如果没有,删除套餐组关系表
        setmealDao.deleteSetMealCheckGroupById(id);
        //最后删除套餐
        setmealDao.deleteSetMealById(id);
    }

    /**
     * 查找数据上的所有图片
     *
     * @Date 2020/9/22 17:42
     * @param:
     **/
    @Override
    public List<String> findAllImgs() {

        List<String> list = setmealDao.findAllImgs();

        return list;
    }

    /**
     * 套餐列表展示
     *
     * @Date 2020/9/23 17:58
     * @param:
     **/
    @Override
    public List<Setmeal> findAllSetmeal() {

        List<Setmeal> setmeals = setmealDao.findAllSetmeal();
        return setmeals;
    }

    /**
     * 查询套餐详情
     *
     * @Date 2020/9/23 18:30
     * @param: id
     **/
    @Override
    public Setmeal findDetailById(Integer id) {

        Setmeal setmeal = setmealDao.findDetailById(id);

        return setmeal;
    }

    /**
     * 查询套餐详情2
     *
     * @Date 2020/9/23 18:30
     * @param: id
     **/
    @Override
    public Setmeal findDetailById2(Integer id) {
        Setmeal setmeal = setmealDao.findDetailById2(id);

        return setmeal;

    }

    @Override
    public Setmeal findById(Integer id) {

        Setmeal setmeal = setmealDao.findById(id);
        return setmeal;
    }

    /**
     * 获取套餐预约占比
     *
     * @Date 2020/9/28 18:24
     * @param:
     **/
    @Override
    public List<Map<String, Object>> findSetmealCount() {

        List<Map<String, Object>> mapList = setmealDao.findSetmealCount();

        return mapList;
    }
}
