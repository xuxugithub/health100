package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.StringUtil;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import com.itheima.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/25 17:59
 */
@RestController
@RequestMapping("/order")
public class OrderController {


    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 体检预约
     *
     * @Date 2020/9/25 18:20
     * @param: orderInfo
     **/
    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String, String> orderInfo) {
        //判断验证码
        Jedis jedis = jedisPool.getResource();
        //key
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + orderInfo.get("telephone");
        String value = jedis.get(key);
        //如果redis中没有值
        if (StringUtil.isEmpty(value)) {

            //没有验证码
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
            //存到redis中

        }
        //如果有值判断 是否和redis中一致
        if (!value.equals(orderInfo.get("validateCode"))) {

            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        //如果值相同
        //删除key
        // jedis.del(key);

        Order order = orderService.submit(orderInfo);
        order.setOrderType("微信预约");
        return new Result(true, MessageConstant.ORDER_SUCCESS, order);
    }


    //预约成功
    @GetMapping("/findById")
    public Result findById(Integer id) {
        Map<String,String> orderInfo = orderService.findById(id);

        return new Result(true,MessageConstant.ORDER_SUCCESS,orderInfo);
    }
}
