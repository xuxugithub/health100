package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetmealServicce;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiContext;
import org.jxls.util.JxlsHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
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
    private ReportService reportService;

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
            calendar.add(Calendar.MONTH, 1);
            //获取时间
            Date date = calendar.getTime();
            //添加进集合
            months.add(sdf.format(date));

        }

        //memberCount
        //通过月获取每月的会员数量
        List<Integer> memberCount = memberService.findMemberCountByDate(months);

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("months", months);
        resultMap.put("memberCount", memberCount);

        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, resultMap);
    }

    /**
     * 获取套餐预约占比
     *
     * @Date 2020/9/28 18:08
     * @param:
     **/
    @GetMapping("/getSetmealReport")
    public Result getSetmealReport() {

        //获取套餐名称
        List<String> setmealNames = new ArrayList<>();
        //获取套餐名和数量
        List<Map<String, Object>> setmealCount = setmealServicce.findSetmealCount();
        if (setmealCount != null) {
            for (Map<String, Object> map : setmealCount) {
                setmealNames.add((String) map.get("name"));

            }
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("setmealNames", setmealNames);
        resultMap.put("setmealCount", setmealCount);
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, resultMap);
    }

    /**
     * 运营数据统计
     *
     * @Date 2020/9/28 19:57
     * @param:
     **/
    @GetMapping("/getBusinessReportData")
    public Result getBusinessReportData() {

        Map<String, Object> resultMap = memberService.getBusinessReportData();
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, resultMap);

    }

    /**
     * 导出模版
     *
     * @Date 2020/9/28 22:57
     * @param: request
     * @param: response
     **/
    @GetMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> resultMap = memberService.getBusinessReportData();

        //获取模版路径
        String template = request.getSession().getServletContext().getRealPath("/template/report_template.xlsx");
        try {
            //创建工作簿
            //创建输出流
            Workbook workbook = new XSSFWorkbook(template);
            ServletOutputStream os = response.getOutputStream();

            //获取表
            Sheet sheetAt = workbook.getSheetAt(0);
            sheetAt.getRow(2).getCell(5).setCellValue((String) resultMap.get("reportDate"));
            //会员统计
            sheetAt.getRow(4).getCell(5).setCellValue((int) resultMap.get("todayNewMember"));
            sheetAt.getRow(5).getCell(5).setCellValue((int) resultMap.get("thisWeekNewMember"));
            sheetAt.getRow(4).getCell(7).setCellValue((int) resultMap.get("totalMember"));
            sheetAt.getRow(5).getCell(7).setCellValue((int) resultMap.get("thisMonthNewMember"));
            //预约到诊数据统计
            // ============= 预约到诊数据统计 ===============
            sheetAt.getRow(7).getCell(5).setCellValue((int) resultMap.get("todayOrderNumber"));
            sheetAt.getRow(7).getCell(7).setCellValue((int) resultMap.get("todayVisitsNumber"));
            sheetAt.getRow(8).getCell(5).setCellValue((int) resultMap.get("thisWeekOrderNumber"));
            sheetAt.getRow(8).getCell(7).setCellValue((int) resultMap.get("thisWeekVisitsNumber"));
            sheetAt.getRow(9).getCell(5).setCellValue((int) resultMap.get("thisMonthOrderNumber"));
            sheetAt.getRow(9).getCell(7).setCellValue((int) resultMap.get("thisMonthVisitsNumber"));
            //热门套餐
            int rowIndex = 12;
            List<Map<String, Object>> hotSetmeal = (List<Map<String, Object>>) resultMap.get("hotSetmeal");
            if (hotSetmeal != null) {
                for (Map<String, Object> setmeal : hotSetmeal) {
                    Row row = sheetAt.getRow(rowIndex);
                    row.getCell(4).setCellValue((String) setmeal.get("name"));
                    row.getCell(5).setCellValue((Long) setmeal.get("setmeal_count"));
                    BigDecimal proportion = (BigDecimal) setmeal.get("proportion");
                    row.getCell(6).setCellValue(proportion.doubleValue());
                    row.getCell(7).setCellValue((String) setmeal.get("remark"));
                    rowIndex++;
                }

            }
            String filename = "运营数据统计.xlsx";
            filename=new String(filename.getBytes(),"ISO-8859-1");
            System.out.println(filename);
            //设置内容体格式
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            //把工作簿输出到输出流
            workbook.write(os);
            //刷新
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 导出运营数据PDF
     *
     * @Date 2020/9/29 20:17
     * @param:
     **/
    @GetMapping("/exportBusinessReportOfPDF")
    public Result exportBusinessReportOfPDF(HttpServletRequest request, HttpServletResponse response) {

        String realPath = request.getSession().getServletContext().getRealPath("/template");
        //指定模板路径
        String jrxml = realPath + File.separator + "report_business.jrxml";
        //编译后的文件
        String jasper = realPath + File.separator + "report_business.jasper";
        Map<String, Object> resultMap = memberService.getBusinessReportData();
        try {
            //编辑模板
            JasperCompileManager.compileReportToFile(jrxml, jasper);
            List<Map<String, Object>> hotSetmeal = (List<Map<String, Object>>) resultMap.get("hotSetmeal");
            //填充数据
            resultMap.put("conpany", "传智健康");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, resultMap, new JRBeanCollectionDataSource(hotSetmeal));
            //设置内容格式
            response.setContentType("application/pdf");
            //设置头信息
            response.setHeader("Content-Disposition", "attachement;filename=bussinessReport.pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

            return null;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return new Result(false, "导出运营数据PDF失败");
    }
    /**
     * 导出运营数据2
     *
     * @Date 2020/9/29 20:40
     * @param:
     **/
    @GetMapping("/exportBusinessReport2")
    public void exportBusinessReport2(HttpServletRequest req, HttpServletResponse res) {
        // 获取报表数据
        Map<String, Object> reportData =memberService.getBusinessReportData() ;
        // 获取模板路径

        String template = req.getSession().getServletContext().getRealPath("/template/report_template2.xlsx");
        // 通过模板创建工作簿
        try(Workbook wk = new XSSFWorkbook(template);
            ServletOutputStream os = res.getOutputStream();
        ) {
            // 数据模型
            Context context = new PoiContext();
            context.putVar("obj", reportData);

            // 设置内容体格式excel
            res.setContentType("application/vnd.ms-excel");
            // 设置输出流的头信息，告诉浏览器，这是文件下载
            String filename = "运营数据统计.xlsx";
            filename = new String(filename.getBytes(),"ISO-8859-1");
            res.setHeader("Content-Disposition","attachment;filename=" + filename);
            // 把工作簿输出到输出流
            // 把数据模型中的数据填充到文件中
            JxlsHelper.getInstance().processTemplate(new FileInputStream(template),res.getOutputStream(),context);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
