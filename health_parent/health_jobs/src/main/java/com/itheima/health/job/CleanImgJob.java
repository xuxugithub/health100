package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;

import com.itheima.health.service.SetmealServicce;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/22 17:30
 */
@Component("cleanImgJob")
public class CleanImgJob {
    //订阅服务
    @Reference
    private SetmealServicce setmealServicce;

    public void cleanImg(){
        //获取七牛云上的图片
        List<String> qiNiuIms = QiNiuUtils.listFile();
        //获得数据库中的图片
        List<String> imgs=setmealServicce.findAllImgs();
        //两个相减
        //不用的照片
        qiNiuIms.removeAll(imgs);
        //返回字符串对象数组
        System.out.println();
        String[] strings = qiNiuIms.toArray(new String[] {});
        //删除七牛上的垃圾图片
        QiNiuUtils.removeFiles(strings);
    }


}
