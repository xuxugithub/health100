package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/19 22:17
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckgroupController {
    //订阅
    @Reference
    private CheckGroupService checkGroupService;



    /**通过id删除检查组
     * @Date 2020/9/20 16:22
     * @param: checkGroup
     * @param: checkitemIds
     **/
    @GetMapping("/deleteById")
    public Result deleteById(int id) {

        checkGroupService.deleteById(id);
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    /**编辑检查组
     * @Date 2020/9/20 15:15
     * @param: checkGroup
 * @param: checkitemIds
     **/
    @PostMapping("/update")
    public Result update(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds) {

        checkGroupService.update(checkGroup,checkitemIds);
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }


    /**根据检查组id去查检查组和检查项
     * @Date 2020/9/20 14:11
     * @param: checkGroup
     * @param: checkitemIds
     **/
    @GetMapping("/findById")
    public Result findById(int id) {
      CheckGroup checkGroup=  checkGroupService.findById(id);
        return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
    }
        /**新增检查组
         * @Date 2020/9/19 22:19
         * @param: null
         **/
    @PostMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup,  Integer[] checkitemIds){

        checkGroupService.add(checkGroup,checkitemIds);

        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }


    /**分页查询
     * @Date 2020/9/20 13:29
     * @param: queryPageBean
     **/
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {

          PageResult pageResult= checkGroupService.findPage(queryPageBean);

          return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,pageResult);
    }
    }
