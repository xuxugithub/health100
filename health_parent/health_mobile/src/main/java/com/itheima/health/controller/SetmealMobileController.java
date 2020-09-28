package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealServicce;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/23 17:50
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {

    @Reference
    private SetmealServicce setmealServicce;

    /**
     * 展示套餐信息
     *
     * @Date 2020/9/23 17:52
     * @param:
     **/
    @GetMapping("/getSetmeal")
    public Result getSetmeal() {

        List<Setmeal> list = setmealServicce.findAllSetmeal();
        //拼接路径 ,并返回
        for (Setmeal setmeal : list) {
            setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        }
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, list);

    }

    //findDetailById
    @GetMapping("/findDetailById")
    public Result findDetailById(Integer id) {

        //
     Setmeal setmeal =  setmealServicce.findDetailById(id);
        //回显图片
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true,MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmeal);
    }
    @GetMapping("/findDetailById2")
    public Result findDetailById2(Integer id) {
        Setmeal setmeal =  setmealServicce.findDetailById2(id);
        //回显图片
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true,MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmeal);

    }

    @GetMapping("/findById")
    public Result findById(Integer id){

        Setmeal setmeal=setmealServicce.findById(id);
        setmeal.setImg(QiNiuUtils.DOMAIN+setmeal.getImg());
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

    }
