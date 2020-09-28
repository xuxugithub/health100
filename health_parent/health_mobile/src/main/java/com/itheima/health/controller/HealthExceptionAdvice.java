package com.itheima.health.controller;


import com.itheima.health.controller.exception.HealthException;
import com.itheima.health.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** 自定义异常
 * 区别系统异常还是自定义异常
 * 友好提示
 *终止不符合业务逻辑的代码
 * @Author: xuyanwei
 * @Date: 2020/9/19 20:17
 */

@RestControllerAdvice
public class HealthExceptionAdvice {


    private static final Logger log= LoggerFactory.getLogger(HealthExceptionAdvice.class);

    /**处理自定义异常
     * @Date 2020/9/19 20:31
     * @param: e
     **/
    @ExceptionHandler(HealthException.class)
    public Result handleHealthException(HealthException e){
        return  new Result(false,e.getMessage());
    }

    /**处理其他异常
     * @Date  20:31
     * @param: ex
     **/

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception ex){

    log.error("发生异常",ex);

    return new Result(false,"发生未知异常,请联系管理员");

    }
}
