package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.StringUtil;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.OrderService;

import com.itheima.health.service.SetmealServicce;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;

import java.util.Date;
import java.util.List;
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
    @Reference
    private SetmealServicce setmealServicce;

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
        Map<String,Object> orderInfo = orderService.findById(id);

        return new Result(true,MessageConstant.ORDER_SUCCESS,orderInfo);
    }

    /**导出预约成功信息
     * @Date 2020/9/29 17:35
     * @param: id
     **/
    @GetMapping("/exportSetmealInfo")
    public void exportSetmealInfo(int id, HttpServletResponse response){

        Map<String,Object> orderInfo = orderService.findById(id);
        //套餐详情
        Integer setmealId = (Integer)orderInfo.get("setmeal_id");
        Setmeal setmeal = setmealServicce.findDetailById(setmealId);
        //写到pdf里
        Document document=new Document();
        //保存到输出流
        //设置类型
        try {
            response.setContentType("application/pdf");

            response.setHeader("Content-Dispostion","attachement;filename=setmealInfo.pdf");
            PdfWriter.getInstance(document,response.getOutputStream());
            //打开文档
            document.open();
            //写内容
            //设置表格字体
            BaseFont cn = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H",false);
            Font font = new Font(cn, 10, Font.NORMAL, Color.BLUE);
            document.add(new Paragraph("体检人:"+(String) orderInfo.get("memeber"),font));
            document.add(new Paragraph("体检套餐:"+ orderInfo.get("setmeal"),font));
            document.add(new Paragraph("体检日期:"+orderInfo.get("orderDate"),font)) ;
            document.add(new Paragraph("预约类型:"+orderInfo.get("orderType"),font));
            document.add(new Paragraph(""));
            document.add(new Paragraph("套餐信息:",font));

            // 套餐详情
            Table table = new Table(3); // 3列  表头

            //======================== 表格样式 ========================
            // 向document 生成pdf表格
            table.setWidth(80); // 宽度
            table.setBorder(1); // 边框
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); //水平对齐方式
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP); // 垂直对齐方式
            /*设置表格属性*/
            table.setBorderColor(new Color(0, 0, 255)); //将边框的颜色设置为蓝色
            table.setPadding(5);//设置表格与字体间的间距
            //table.setSpacing(5);//设置表格上下的间距
            table.setAlignment(Element.ALIGN_CENTER);//设置字体显示居中样式
            table.addCell(buildCell("项目名称",font));
            table.addCell(buildCell("项目内容",font));
            table.addCell(buildCell("项目解读",font));
            // 检查组
            List<CheckGroup> checkGroups = setmeal.getCheckGroups();
            if(null != checkGroups){
                for (CheckGroup checkGroup : checkGroups) {
                    // 检查组名称
                    table.addCell(buildCell(checkGroup.getName(),font));
                    // 检查项的名称 拼接起来的
                    StringBuilder sb = new StringBuilder();
                    List<CheckItem> checkItems = checkGroup.getCheckItems();
                    if(null != checkItems){
                        for (CheckItem checkItem : checkItems) {
                            sb.append(checkItem.getName()).append(" ");
                        }
                        // 去除最后的空格
                        sb.setLength(sb.length()-1);
                    }
                    table.addCell(buildCell(sb.toString(),font));
                    // 解读
                    table.addCell(buildCell(checkGroup.getRemark(),font));
                }
            }
            document.add(table);

            //关闭文档
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //设置内容

        //设置头
    }

    //传递内容和字体样式,生成单元格
    private Cell buildCell(String content, Font font) throws BadElementException {
        Phrase phrase = new Phrase(content, font);
        return new Cell(phrase);

    }
}
