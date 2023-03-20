package com.atguigu.controller;

import com.atguigu.base.BaseController;
import com.atguigu.entity.Permission;
import com.atguigu.service.PermissionService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/permission")
public class PermissionController extends BaseController {

    private static final String PAGE_INDEX = "permission/index";
    private static final String PAGE_CREATE = "permission/create";
    private static final String PAGE_EDIT = "permission/edit";
    private static final String LIST_ACTION = "redirect:/permission";
    @DubboReference
    private PermissionService permissionService;

    /**
     * 跳转菜单管理页面
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission.show')")
    @RequestMapping
    public String index(Model model){
        List<Permission> list = permissionService.findPermissionHelperAll();
        model.addAttribute("list",list);

        return PAGE_INDEX;
    }

    /**
     * 跳转新增菜单页面
     * @param parentId
     * @param type
     * @param parentName
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission.create')")
    @GetMapping("/create")
    public String create(Long parentId,Integer type,String parentName,Model model){
        Permission permission = new Permission();
        permission.setParentId(parentId);
        permission.setType(type);
        permission.setParentName(parentName);

        model.addAttribute("permission",permission);
        return PAGE_CREATE;
    }

    /**
     * 新增权限
     * @param permission
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission.create')")
    @PostMapping("/save")
    public String save(Permission permission,Model model){
        permissionService.insert(permission);
        return successPage(model,"新增权限成功!");
    }

    /**
     * 跳转修改权限页面
     * @param id
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission.edit')")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,Model model){
        Permission permission = permissionService.getById(id);
        model.addAttribute("permission",permission);
        return PAGE_EDIT;
    }

    /**
     * 修改权限
     * @param permission
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission.edit')")
    @PostMapping("/update/{id}")
    public String update(Permission permission,Model model){
        permissionService.update(permission);
        return successPage(model,"修改权限成功!");
    }

    /**
     * 删除权限
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('permission.delete')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        permissionService.delete(id);
        return LIST_ACTION;
    }

}
