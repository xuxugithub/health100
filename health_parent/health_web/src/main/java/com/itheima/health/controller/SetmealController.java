package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealServicce;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/20 19:11
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Reference
    private SetmealServicce setmealServicce;
    //注入jedis连接池
    @Autowired
    private JedisPool jedisPool;
    /**
     * 上传图片
     *
     * @Date 16:46
     * @param: id
     **/
    @PostMapping("/upload")
    public Result upload(MultipartFile imgFile) {
        //截取文件名的后缀名
        try {
            String filename = imgFile.getOriginalFilename();
            //从.开始截取
            String substring = filename.substring(filename.lastIndexOf("."));
            //生成唯一文件名
            String imgName = UUID.randomUUID() + substring;
            //用七牛云上传文件
            QiNiuUtils.uploadViaByte(imgFile.getBytes(),imgName);
            //获取图片的域
            String domain = QiNiuUtils.DOMAIN;
            Map<String,String> map=new HashMap<>();
            map.put("imgName",imgName);
            map.put("domain",domain);
            return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
    }

    /**
     * 回显套餐数据
     * @Date 2020/9/20 22:33
     * @param: id
     **/
    @GetMapping("/findSetmealById")
    public Result findById(Integer id) {
        Map<String, Object> resultMap = setmealServicce.findSetmealById(id);
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, resultMap);
    }
    /**
     * 新增套餐
     * @Date 2020/9/20 22:01
     * @param: queryPageBean
     **/
    @PostMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {

        setmealServicce.add(setmeal, checkgroupIds);
        //获取jedis
        Jedis jedis = jedisPool.getResource();
        //保存的数据格式为  :套餐id|操作类型|时间戳
        jedis.sadd("setmeal:static:html",setmeal.getId()+"|1|"+System.currentTimeMillis());
        //换回连接池
        jedis.close();
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 分页查询套餐
     *
     * @Date 2020/9/20 19:28
     * @param: queryPageBean
     * @RequestBody只能用于post提交方法(request提交方法没有方法体)
     **/
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {

        PageResult pageResult = setmealServicce.findPage(queryPageBean);

        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, pageResult);
    }

    /**
     * 查询所有检查组
     *
     * @Date 2020/9/20 20:26
     * @param: queryPageBean
     **/
    @GetMapping("/findAll")
    public Result findAll() {

        List<CheckGroup> checkGroupList = setmealServicce.findAll();
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroupList);
    }

    /**获取所有被选中的检查组id
     * @Date 2020/9/21 19:49
     * @param:
     **/
    @GetMapping("/findAllCheckedId")
    public Result findAllCheckedId(Integer id) {

     List<Integer> list=  setmealServicce.findAllCheckedId(id);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, list);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Setmeal setmeal, Integer[] checkgroupIds) {

        setmealServicce.update(setmeal, checkgroupIds);
        Jedis jedis = jedisPool.getResource();
        //保存的数据类型 1代表创建
        jedis.sadd("setmeal:static:html",setmeal.getId()+"|1|"+System.currentTimeMillis());
        jedis.close();

        return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

    /**删除套餐
     * @Date 2020/9/21 21:36
     * @param: id
     **/
    @GetMapping("/delete")
    public Result delete(Integer id) {

        setmealServicce.delete(id);
        Jedis jedis = jedisPool.getResource();
      //保存的数据类型 0代表删除
        jedis.sadd("setmeal:static:html",id+"|0|"+System.currentTimeMillis());
        jedis.close();

        return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
    }
}
