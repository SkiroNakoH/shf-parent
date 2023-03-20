package com.atguigu.controller;

import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Role;
import com.atguigu.result.Result;
import com.atguigu.service.AdminService;
import com.atguigu.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {

    private static final String PAGE_INDEX = "admin/index";
    private static final String PAGE_CREATE = "admin/create";
    private static final String PAGE_EDIT = "admin/edit";
    private static final String LIST_INDEX = "redirect:/admin";
    private static final String PAGE_UPLOAD = "admin/upload";
    private static final String PAGE_ASSIGNSHOW = "admin/assignShow";
    @DubboReference
    public AdminService adminService;

    /**
     * 用户管理首页
     *
     * @param filters
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('admin.show')")
    @RequestMapping
    public String index(@RequestParam Map filters, Model model) {
        //分页查询
        model.addAttribute("filters", filters);

        PageInfo<Admin> pageInfo = adminService.findPage(filters);
        model.addAttribute("page", pageInfo);

        return PAGE_INDEX;
    }

    /**
     * 跳转新增用户页面
     *
     * @return
     */
    @PreAuthorize("hasAnyAuthority('admin.create')")
    @RequestMapping("/create")
    public String create() {
        return PAGE_CREATE;
    }

    /**
     * 新增用户
     *
     * @param admin
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('admin.create')")
    @RequestMapping("/save")
    public String save(Admin admin, Model model) {
        //对密码使用BCryptPasswordEncoder加密
        admin.setPassword(new BCryptPasswordEncoder().encode(admin.getPassword()));

        adminService.insert(admin);

        return successPage(model, "新增用户成功!!");
    }

    /**
     * 跳转修改用户页面
     *
     * @param id
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('admin.edit')")
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Admin admin = adminService.getById(id);
        model.addAttribute("admin", admin);

        return PAGE_EDIT;
    }

    /**
     * 修改用户
     *
     * @param admin
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('admin.edit')")
    @PostMapping("/update")
    public String update(Admin admin, Model model) {
        adminService.update(admin);
        return successPage(model, "修改用户成功!!");
    }

    /**
     * 跳转删除用户页面
     *
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('admin.delete')")
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        adminService.delete(id);
        return LIST_INDEX;
    }

    /**
     * 跳转用户头像上传页面
     *
     * @param id
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('admin.edit')")
    @RequestMapping("/uploadShow/{id}")
    public String uploadShow(@PathVariable Long id, Model model) {
        model.addAttribute("id", id);
        return PAGE_UPLOAD;
    }

    /**
     * 上传用户头像
     *
     * @param id
     * @param file
     * @return
     */
    @PreAuthorize("hasAnyAuthority('admin.edit')")
    @RequestMapping("/upload/{id}")
    public String upload(@PathVariable Long id, @RequestParam("file") MultipartFile file, Model model) {
        try {
            String newFileName = UUID.randomUUID().toString().replace("-", "") + "." +
                    file.getOriginalFilename().split("\\.")[1];
            QiniuUtils.upload2Qiniu(file.getBytes(), newFileName);

            Admin admin = new Admin();
            admin.setId(id);
            admin.setHeadUrl(QiniuUtils.getUrlName(newFileName));

            adminService.update(admin);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return successPage(model, "头像上传成功！");
    }

    /**
     * 跳转角色分配页面
     *
     * @param adminId 用户id
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('admin.assgin')")
    @RequestMapping("/assignShow/{id}")
    public String assignShow(@PathVariable("id") Long adminId, Model model) {
        model.addAttribute("adminId", adminId);

        //根据用户id查询角色列表,包含assignRoleList 和 unAssignRoleList
        Map<String, List<Role>> map = adminService.findMapByAdminId(adminId);
        model.addAllAttributes(map);
        return PAGE_ASSIGNSHOW;
    }

    /**
     * 分配角色
     *
     * @param adminId          用户id
     * @param assignRoleIdList 准备授权的角色ids
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('admin.assgin')")
    @PostMapping("/assignRole")
    public String assignRole(Long adminId, @RequestParam("roleIds") List<Long> assignRoleIdList, Model model) {
        adminService.assignRole(adminId, assignRoleIdList);

        return successPage(model, "分配角色成功!");
    }

}
