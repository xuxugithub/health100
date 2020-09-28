package com.itheima.health;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/22 18:01
 */
public class QuartzApplication {


    public static void main(String[] args) throws IOException {

        new ClassPathXmlApplicationContext("classpath:spring-jobs.xml");

        System.in.read();
    }
}
