package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.SetmealServicce;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/28 17:12
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference

    private MemberService memberService;


    @Reference
    private SetmealServicce setmealServicce;
    /**
     * 获取会员统计
     *
     * @Date 2020/9/28 17:26
     * @param:
     **/
    @GetMapping("/getMemberReport")
    public Result getMemberReport() {

        //months
        //组装过去一年的月份
        List<String> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        //获取当前时间
        calendar.setTime(new Date());
        //年份减一
        calendar.add(Calendar.YEAR, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        for (int i = 0; i < 12; i++) {
            //月份加一
            calendar.add(Calendar.MONTH,1);
            //获取时间
            Date date = calendar.getTime();
            //添加进集合
            months.add(sdf.format(date));

        }

        //memberCount
        //通过月获取每月的会员数量
        List<Integer> memberCount=memberService.findMemberCountByDate(months);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("months",months);
        resultMap.put("memberCount",memberCount);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,resultMap);
    }
    /**获取套餐预约占比
     * @Date 2020/9/28 18:08 
     * @param: 
     **/
    @GetMapping("/getSetmealReport")
    public Result getSetmealReport(){

         //获取套餐名称
        List<String> setmealNames=new ArrayList<>();
       //获取套餐名和数量
       List<Map<String,Object>> setmealCount=setmealServicce.findSetmealCount();
        if (setmealCount!=null){
            for (Map<String, Object> map : setmealCount) {
                setmealNames.add((String) map.get("name"));

            }
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("setmealNames",setmealNames);
        resultMap.put("setmealCount",setmealCount);
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,resultMap);
    }
    /**运营数据统计
     * @Date 2020/9/28 19:57
     * @param:
     **/
    @GetMapping("/getBusinessReportData")
    public Result getBusinessReportData(){

        Map<String, Object> resultMap = memberService.getBusinessReportData();
        return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,resultMap);

    }
    /**导出模版
     * @Date 2020/9/28 22:57
     * @param: request
     * @param: response
     **/
    @GetMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        //获取模版路径
        String template = request.getSession().getServletContext().getRealPath("/template/report_template.xlsx");
        try {
            //创建工作簿
            //创建输出流
            ServletOutputStream os = response.getOutputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(template);
            //
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
