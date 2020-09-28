package com.itheima.health.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.controller.exception.HealthException;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/18 17:15
 */
//发布服务
@Service
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 检查项列表
     *
     * @return java.util.List<com.itheima.health.pojo.CheckItem>
     * @Date 2020/9/19 17:07
     * @param:
     **/
    @Override
    public List<CheckItem> findAll() {
        List<CheckItem> checkItemList = checkItemDao.findAll();
        return checkItemList;
    }

    /**
     * 分页查询
     *
     * @return com.itheima.health.entity.PageResult
     * @Date 2020/9/19 17:07
     * @param: queryPageBean
     **/
    @Override
    public PageResult findByPage(QueryPageBean queryPageBean) {
        //分页查询
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //如果查询条件不为空,拼接"%"
        if (!StringUtil.isEmpty(queryPageBean.getQueryString())) {
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        //page分页查询
        Page<CheckItem> page = checkItemDao.findByPage(queryPageBean.getQueryString());
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }

    /**
     * @Date 2020/9/21 18:01
     * @param: checkItem
     **/
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);

    }

    /**
     * 删除检查项
     *
     * @Date 2020/9/19 19:51
     * @param: id
     **/
    @Override
    @Transactional
    public void delete(int id) throws HealthException {
        //去查询关联表是否有id,有则不能删
        int count = checkItemDao.findById(id);
        if (count > 0) {
            throw new HealthException("操作失败,该体检项组里有该体检项,不能删除");

        }

        checkItemDao.delete(id);
    }
    /**回显检查项数据
     * @Date 2020/9/19 21:43
     * @param: id
     **/
    @Override
    public CheckItem editById(int id) {
        CheckItem checkItem = checkItemDao.edit(id);
        return checkItem;
    }
    /**更改检查项数据
     * @Date 2020/9/19 21:43
     * @param: checkItem
     **/
    @Override
    public void update(CheckItem checkItem) {
        checkItemDao.update(checkItem);
    }


}
