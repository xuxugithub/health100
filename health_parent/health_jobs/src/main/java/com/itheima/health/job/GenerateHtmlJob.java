package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealServicce;
import com.itheima.health.utils.QiNiuUtils;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/24 9:52
 */
@Component
public class GenerateHtmlJob {

    //日志
    Object target;
    private static  final  Logger log= LoggerFactory.getLogger(GenerateHtmlJob.class);
    //初始化方法
    @PostConstruct
    private void init(){
        //设置模版所在的位置

        configuration.setTemplateLoader(new ClassTemplateLoader(this.getClass(),"/ftl"));
        //设置默认编码
        configuration.setDefaultEncoding("utf-8");

    }
    //jedis连接池
    @Autowired
    private JedisPool jedisPool;
    //订阅套餐服务
    @Reference
    private SetmealServicce setmealServicce;

    //注入freemarker主配置类
    @Autowired
    private Configuration configuration;

    //生成静态页面存放的目录
    @Value("${out_put_path}")
    private  String out_put_path;

    //任务执行的方法
    @Scheduled(initialDelay = 3000,fixedDelay = 1800000)
    public void doGenerateHtml(){

        //获取jedis连接
        Jedis jedis = jedisPool.getResource();
        String key="setmeal:static:html";
        //获得所有套餐id数据
        Set<String> smembers = jedis.smembers(key);
        if(smembers!=null&&smembers.size()>0){
         //套餐id不为空
            for (String setmealId : smembers) {
                //把字符串以|分割
                String[] ss = setmealId.split("\\|");
                //套餐id
                int id = Integer.parseInt(ss[0]);
                //操作类型
                String value = ss[1];
             if ("1".equals(value)) {
                 //操作1 生成套餐详情页面
                //查询套餐详情
                 Setmeal setmeal = setmealServicce.findDetailById2(id);
                 //设置图片的路径
                 setmeal.setImg(QiNiuUtils.DOMAIN+setmeal.getImg());
                 //生成静态页面
                 generateSetmealDetailHtml(setmeal);
             }else {

                    //操作类型 0  删除已经生成的页面
                    removeSetmealDetailHtml(id);

             }
                    //处理完 删除 jedis数据
                jedis.srem(key,setmealId);
            }
            //重新生成静态页面
            generateSetmeaList();
        }



    }
    /**重新生成套餐列表页面
     * @Date 2020/9/24 10:33
     * @param:
     **/
    private void generateSetmeaList() {
        List<Setmeal> setmealList = setmealServicce.findAllSetmeal();
        setmealList.forEach(setmeal -> {
           setmeal.setImg(QiNiuUtils.DOMAIN+setmeal.getImg());
        });
        Map<String, Object> dataMap=new HashMap<>();
        //添加数据到map   setmealList要与模版中的数据一致
        dataMap.put("setmealList",setmealList);
        //生成模版名称
        String templateName="mobile_setmeal.ftl";
        //全路径
        String fileName=out_put_path+"/mobile_setmeal.html";

        //生成文件
        generateFile(dataMap,templateName,fileName);
    }

    /**删除存在的页面
         * @Date 2020/9/24 10:31
         * @param: id
         **/
    private void removeSetmealDetailHtml(int id) {
        //根据父路径和子路径拼接成新路径 new File(String parent,String child )
        File file = new File(out_put_path,String.format("setmeal_%d",id));
        //如果文件存在 就删除
        if(file.exists()){
            file.delete();
        }

    }
    /**生成套餐详情页面
     * @Date 2020/9/24 10:30
     * @param: setmeal
     **/
    private void generateSetmealDetailHtml(Setmeal setmeal) {
        //填充数据
        Map<String,Object> map=new HashMap<>();
        //与模版名称一致
        map.put("setmeal",setmeal);
        //模板名
        String name="mobile_setmeal_detail.ftl";
        //填写全路径
        String fileName=String.format("%s/setmeal_%d.html",out_put_path,setmeal.getId());
        //生成文件
        generateFile(map,name,fileName);
    }
    /**生成文件
     * @Date 2020/9/24 11:28
     * @param: map (填充模板数据)
     * @param: name(模板名称)
     * @param: fileName(文件全路径)
     **/
    private void generateFile(Map<String, Object> map, String name, String fileName) {
        try {
            //获取模版 (模板名称)
            Template template = configuration.getTemplate(name);
            //读取文件 全路径 设置编码
            OutputStream out;
            BufferedWriter bfw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName),"UTF-8"));
            //填充数据到模板
            template.process(map,bfw);
            bfw.flush();
            bfw.close();
        } catch (Exception e) {
            log.error("生成静态页面失败",e);
            e.printStackTrace();
        }


    }

/*    public static void main(String[] args) {
        String fileName=String.format("%s/setmeal_%d.html","3/2/4",2);
        System.out.println(fileName);

    }*/

}
