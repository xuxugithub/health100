package com.itheima.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.utils.SMSUtils;
import com.itheima.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/25 17:20
 */

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {


    @Autowired
    private JedisPool jedisPool;

    /**
     * 发送验证码
     *
     * @Date 2020/9/25 17:23
     * @param: telephone
     **/
    @GetMapping("/send4Order")
    public Result send4Order(String telephone) {
        //将验证码存到redis中

        //判断redis中是否有该key
        Jedis jedis = jedisPool.getResource();

        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        String value = jedis.get(key);
        try {
            if (value == null) {
                //验证码为空
                //生成验证码
                String code = ValidateCodeUtils.generateValidateCode4String(6);
                //存到redis中
                jedis.setex(key, 15 * 80, code);
                //发送短信验证码
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code);
                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } else {
                //不为空
                return new Result(false, MessageConstant.SEND_VALIDATECODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }


    /**
     * 校验登录验证码
     *
     * @Date 2020/9/27 14:56
     * @param: telephone
     **/
    @GetMapping("/send4Login")
    public Result send4Login(String telephone) {
        //
        Jedis jedis = jedisPool.getResource();
        String key = RedisMessageConstant.SENDTYPE_LOGIN+ "_" + telephone;
        String value = jedis.get(key);
        try {
            if (value == null) {
                //发送验证码
                String code = ValidateCodeUtils.generateValidateCode4String(6);
                //存到redis中
                jedis.setex(key,15*80,code);
                //发送短信
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code);
                return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);

            }else {
                return new Result(false, MessageConstant.SEND_VALIDATECODE);
            }
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }
}
