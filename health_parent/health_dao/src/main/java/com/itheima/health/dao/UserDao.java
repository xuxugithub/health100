package com.itheima.health.dao;

import com.itheima.health.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/26 20:47
 */
public interface UserDao {

    /**登录验证
     * @Date 2020/9/26 20:47
     * @param: username
     **/
    User findByName(String username);

    /**查询用户的所有菜单
     * @Date 2020/9/30 13:45
     * @param: username
     **/
    List<Map<String, Object>> findAllMenu(String username);

    List<Map<String, Object>> findAllSonMenu(@Param("username") String username, @Param("id")Integer id);
}
