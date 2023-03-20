package com.atguigu.config;

import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import com.github.pagehelper.util.StringUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @DubboReference
    private AdminService adminService;

    @DubboReference
    private PermissionService permissionService;

    /**
     * 登录账户验证和权限管理
     *
     * @param username 登录用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminService.getByName(username);
        if (admin == null)
            return null;

        //查询用户权限
        List<String> codeList = permissionService.findCodeListByAdminId(admin.getId());

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        for (String code : codeList) {
            if (StringUtil.isEmpty(code))
                continue;

            //为用户添加权限
            authorityList.add(new SimpleGrantedAuthority(code));
        }
        return new User(username, admin.getPassword(), authorityList);

    }
}
