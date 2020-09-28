package com.itheima.health;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/18 16:47
 */
public class ProviderApplication {

    public static void main(String[] args) throws IOException {
        //启动容器 dubbo会去注册与发布

        new ClassPathXmlApplicationContext("classpath:applicationContext-service.xml");
        System.in.read();
    }
}
