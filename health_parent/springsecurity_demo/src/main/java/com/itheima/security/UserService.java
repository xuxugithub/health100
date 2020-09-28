package com.itheima.security;

import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: xuyanwei
 * @Date: 2020/9/26 17:43
 */
public class UserService  implements UserDetailsService {

    /**通过用户名加载用户信息
     * @Date 2020/9/26 17:46
     * @param: s
     **/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            //提供用户名 ,密码, 权限集合
            //通过用户名查询数据库  获取角色权限

           com.itheima.health.pojo.User userIndb=findByName(username);
           //创建权限集合
        List<GrantedAuthority> authorityList=new ArrayList<>();
        //获得所有角色
        Set<Role> roles = userIndb.getRoles();
        if(roles!=null){
            for (Role role : roles) {
               //授予的权限
                //构造方法要的是一个角色名
                SimpleGrantedAuthority ga = new SimpleGrantedAuthority(role.getKeyword());
                authorityList.add(ga);
                Set<Permission> permissions = role.getPermissions();
                if(permissions!=null){
                    for (Permission permission : permissions) {
                        SimpleGrantedAuthority pa = new SimpleGrantedAuthority(permission.getKeyword());
                        authorityList.add(pa);
                    }

                }

            }

        }
        //需要 用户名  密码  和权限集合
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(username, userIndb.getPassword(), authorityList);

        return  user;
    }

    //假装从数据库查
    private User findByName(String username) {
        if("admin".equals(username)){
            User user = new User();
            user.setUsername("admin");
            user.setPassword("$2a$10$3lOTscvuTm0TRSpwA06rQeqLSN8aL9NSVeAY/cCzDQEwPxdX8dA3C");

            // t_user > t_role > t_permission
            Role role = new Role();
            role.setName("ROLE_ADMIN");
            role.setKeyword("ROLE_ADMIN");

            Permission permission = new Permission();
            permission.setName("新增检查项");
            permission.setKeyword("ADD_CHECKITEM");
            // 权限是属于某个角色下
            role.getPermissions().add(permission);

            Set<Role> roleList = new HashSet<Role>();
            roleList.add(role);

            // 多种校验规则
            role = new Role();
            role.setName("ABC");
            role.setKeyword("ABC");
            roleList.add(role);

            user.setRoles(roleList);

            return  user;


        }
        return null;
    }
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 加密
        //System.out.println(encoder.encode("1234"));
        //System.out.println(encoder.encode("1234"));
        System.out.println(encoder.encode("admin"));

        // 验证密码
        System.out.println(encoder.matches("1234", "$2a$10$VroLvX/KE54Kn2DohBeRb.Nrw06cbdopZH1A8V22dNDE.3Fsl8z7e"));
        System.out.println(encoder.matches("1234", "$2a$10$YQMDB/SvDAcZ1M9qSd2Dx.yJPuJENMVxkoBqz5HEqRRVtBLUGSLAK"));
        System.out.println(encoder.matches("1234", "$2a$10$C.IdijAh2uJBJDEVhvS2.OSzwzBmbKPDMW3gFopMF0pUIMr.W8La2"));
        System.out.println(encoder.matches("1234", "$2a$10$u/BcsUUqZNWUxdmDhbnoeeobJy6IBsL1Gn/S0dMxI2RbSgnMKJ.4a"));
    }
}
