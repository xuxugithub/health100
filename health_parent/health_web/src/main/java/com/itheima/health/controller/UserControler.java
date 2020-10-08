package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;

import com.itheima.health.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/27 16:04
 */

@RestController
@RequestMapping("/user")
public class UserControler {


    @Reference
    private UserService userService;
    /**获取登录用户名
     * @Date 2020/9/27 16:05
     * @param:
     **/
    @GetMapping("/getLoginUsername")
    public Result getLoginUsername(){
        //获取登录用户的信息
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //获取用户名
        String username = user.getUsername();
        //根据用户名去查用户的信息
        List<Map<String, Object>> resultMap= userService.findAllMenu(username);

        Map<String, Object> map=new HashMap<>();
        map.put("username",username);
        map.put("resultMap",resultMap);
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,map);

    }
    //登录成功
    @RequestMapping("/loginSuccess")
    public Result loginSuccess(){
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
    //登录失败
    @RequestMapping("/loginFail")
    public Result loginFail(){
        return new Result(false, "用户名或密码不正确");
    }

}
