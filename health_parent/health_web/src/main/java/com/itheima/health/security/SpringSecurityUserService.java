package com.itheima.health.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/26 20:26
 */
@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userInDb = userService.findByName(username);
        //如果user不为空
        if (userInDb!=null){


        //创建权限集合
        List<GrantedAuthority> authorityList = new ArrayList<>();
        Set<Role> roles = userInDb.getRoles();
        SimpleGrantedAuthority sga = null;
        if (roles != null) {
            //如果权限不为空 遍历
            for (Role role : roles) {
                //授权
                sga = new SimpleGrantedAuthority(role.getKeyword());
                authorityList.add(sga);
                //角色下的所有权限
                Set<Permission> permissions = role.getPermissions();
                if (permissions != null) {
                    for (Permission permission : permissions) {
                        sga = new SimpleGrantedAuthority(permission.getKeyword());
                        authorityList.add(sga);
                    }

                }
            }
        }
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(username, userInDb.getPassword(), authorityList);
        return user;

    }
             return null;


}
}