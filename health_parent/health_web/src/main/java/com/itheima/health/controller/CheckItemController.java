package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;
import java.util.List;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/18 17:02
 */
@RestController
@RequestMapping("/checkItem")
public class CheckItemController {
    //订阅
    @Reference
    private CheckItemService checkItemService;

    /**
     * 检查项列表
     *
     * @return com.itheima.health.entity.Result
     * @Date 2020/9/19 17:08
     * @param:
     **/
    @GetMapping("/findAll")
    public Result findAll() {

        List<CheckItem> checkItemList = checkItemService.findAll();
        //封装数据
        Result result = new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItemList);
        return result;
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public Result add(@RequestBody CheckItem checkItem) {

        checkItemService.add(checkItem);
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /**
     * 检查项列表
     *
     * @return com.itheima.health.entity.Result
     * @Date 2020/9/19 17:08
     * @param: queryPageBean
     **/
    @RequestMapping("/findByPage")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public Result findByPage(@RequestBody QueryPageBean queryPageBean) {

        PageResult pageResult = checkItemService.findByPage(queryPageBean);

        Result result = new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, pageResult);
        return result;
    }

    /**
     * 删除检查项
     *
     * @Date 2020/9/19 19:51
     * @param: id
     **/
    @PostMapping("/delete")
    public Result delete(int id) {

        checkItemService.delete(id);

        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }


    /**
     * 回显数据
     *
     * @Date 2020/9/19 21:14
     * @param: id
     **/
    @GetMapping("/findById")
    public Result findById(int id) {
        CheckItem checkItem = checkItemService.editById(id);

        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS, checkItem);
    }

    @PostMapping("/update")
    public Result update(@RequestBody CheckItem checkItem) {

        checkItemService.update(checkItem);

        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }
}
