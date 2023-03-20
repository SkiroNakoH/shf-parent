package com.atguigu.controller;

import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.service.AdminService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class FrameController {
    @DubboReference
    private AdminService adminService;

    private static final String PAGE_LOGIN = "frame/login";
    private static final String PAGE_INDEX = "frame/index";
    private static final String PAGE_MAIN = "frame/main";


    @GetMapping("/")
    public String index(Model model) {
        //从security容器中获取认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        //根据用户名从数据库中查询用户
        Admin admin = adminService.getByName(user.getUsername());
        model.addAttribute("admin", admin);
      /*
        Long adminId = 1L;
        Admin admin = adminService.getById(adminId);
        model.addAttribute("admin", admin);*/

        //查找权限
        List<Permission> permissionList = adminService.findPermissionListByAdminId(admin.getId());
        model.addAttribute("permissionList",permissionList);
        return PAGE_INDEX;
    }

    @GetMapping("/main")
    public String main() {
        return PAGE_MAIN;
    }

    @RequestMapping("/login.html")
    public String toLoginPage(){
        return PAGE_LOGIN;
    }

}
