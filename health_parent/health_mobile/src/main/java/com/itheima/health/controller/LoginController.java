package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/27 14:41
 */

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    /**
     * 登录验证
     *
     * @Date 2020/9/27 14:58
     * @param: loginInfo
     **/
    @PostMapping("/check")
    public Result check(@RequestBody Map<String, String> loginInfo, HttpServletResponse res) {
        //校验验证码
        Jedis jedis = jedisPool.getResource();
        String telephone = loginInfo.get("telephone");
        //创建key
        String key = RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone;
        String value = jedis.get(key);
        //判断是否为空
        if (value == null) {
            return new Result(false, "请重新获取验证码");

        }
        //输入不一致
        if (!value.equals(loginInfo.get("validateCode"))) {

            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //删除验证码   防止重复提交
        jedis.del(key);
        memberService.check(loginInfo);
        //cookie跟踪
        Cookie cookie = new Cookie("login_member_telephone",telephone);
        //设置cookie时长
        cookie.setMaxAge(30*24*60*60);
        //设置路径
        cookie.setPath("/");
        //发送cookie
        res.addCookie(cookie);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}
