package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.itheima.health.controller.exception.HealthException;
import com.itheima.health.dao.CheckGroupDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/19 22:26
 */
@Service

public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 新增检查组
     *
     * @Date 2020/9/19 22:26
     * @param: checkGroup
     * @param: ids
     **/
    @Override
    @Transactional
    public void add(CheckGroup checkGroup, Integer[] ids) {
        //添加检查组信息
        checkGroupDao.add(checkGroup);
        Integer checkGroupId = checkGroup.getId();

        //添加关系表数据
        //遍历数组
        if (ids != null) {
            for (Integer checkitem_id : ids) {
                checkGroupDao.addById(checkGroupId, checkitem_id);

            }
        }
    }

    /**
     * 分页查询
     *
     * @Date 2020/9/20 13:29
     * @param: queryPageBean
     **/
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //如果查询条件不为空
        if (!StringUtil.isEmpty(queryPageBean.getQueryString())) {
            //拼接"%"
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        //查询
        Page<CheckGroup> page = checkGroupDao.findPage(queryPageBean.getQueryString());
        //封装数据
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;

    }

    /**
     * 根据检查组id去查检查组和检查项
     *
     * @Date 2020/9/20 14:11
     * @param: checkGroup
     * @param: checkitemIds
     **/
    @Override
    public CheckGroup findById(int id) {
        //查询检查组
        CheckGroup checkGroup = checkGroupDao.findById(id);
        //查询该检查组下的检查项
        List<CheckItem> checkItemList = checkGroupDao.findByCheckGroupId(id);
        checkGroup.setCheckItems(checkItemList);
        return checkGroup;
    }

    /**
     * 编辑检查组
     *
     * @Date 2020/9/20 15:15
     * @param: checkGroup
     * @param: checkitemIds
     **/
    @Override
    @Transactional
    public void update(CheckGroup checkGroup, Integer[] checkitemIds) {
        //先去更改主题表
        checkGroupDao.update(checkGroup);

        //删除旧关系
        checkGroupDao.deleteById(checkGroup.getId());
        //添加新关系
        if (checkitemIds != null) {
            //选项不为空
            for (Integer checkitemId : checkitemIds) {
                checkGroupDao.addById(checkGroup.getId(), checkitemId);
            }

        }

    }

    /**
     * 通过id删除检查组
     *
     * @Date 2020/9/20 16:22
     * @param: checkGroup
     * @param: checkitemIds
     **/
    @Override
    @Transactional
    public void deleteById(int id) throws HealthException {
        //先判断套餐和检查组关系表是否有该检查组id
        int cnt = checkGroupDao.findByIdFromSC(id);
        //如果有就抛异常
        if (cnt > 0) {
            throw new HealthException("套餐检查组关系表中有该检查组,不能删除");
        }
        //没有就先去检查组和检查项关系表删除信息
        checkGroupDao.deleteById(id);
        //最后去检查组删除信息

        checkGroupDao.deleteFromCheckGroup(id);

    }
}
