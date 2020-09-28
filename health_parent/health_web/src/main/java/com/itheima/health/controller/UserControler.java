package com.itheima.health.controller;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/27 16:04
 */

@RestController
@RequestMapping("/user")
public class UserControler {


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

        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);


    }
}
