package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.POIUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/22 18:45
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    private static final Logger log = LoggerFactory.getLogger(OrderSettingController.class);
    @Reference
    private OrderSettingService orderSettingService;

    @PostMapping("/upload")
    public Result upload(MultipartFile excelFile){
        //文件上传
    //读取文件
        try {
            List<String[]> strings = POIUtils.readExcel(excelFile);
            //转换为 List<OrderSetting>
            List<OrderSetting> orderSettingList=new ArrayList<OrderSetting>(strings.size());
            OrderSetting os=null;
            SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);
            for (String[] string : strings) {
                //string 代表一行记录
                Date orderDate = sdf.parse(string[0]);
                //0 日期    1数量
                os=new OrderSetting(orderDate,Integer.valueOf(string[1]));
                orderSettingList.add(os);
            }
            orderSettingService.upload(orderSettingList);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("批量导入失败",e);
        }
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);

    }

    //getOrderSettingMonth
    /**日历展示预约信息
     * @Date 2020/9/22 20:35
     * @param: excelFile
     **/
    @GetMapping("/getOrderSettingMonth")
    public Result getOrderSettingMonth( String month){

      List<Map<String,Integer>> list= orderSettingService.getOrderSettingMonth(month);
      return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,list);
    }


    /**更改预约设置信息
     * @Date 2020/9/22 21:18
     * @param: month
     **/
    @PostMapping("/updateNumber")
    public Result updateNumber(@RequestBody OrderSetting orderSetting) {

        orderSettingService.updateNumber(orderSetting);

        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
    }

    }