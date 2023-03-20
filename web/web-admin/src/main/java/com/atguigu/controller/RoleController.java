package com.atguigu.controller;

import com.alibaba.fastjson.JSON;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Permission;
import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

    private static final String PAGE_INDEX = "role/index";
    private static final String PAGE_CREATE = "role/create";
    private static final String LIST_INDEX = "redirect:/role";

    private static final String PAGE_EDIT = "role/edit";
    private static final String PAGE_ASSIGNSHOW = "role/assignShow";
    @DubboReference
    private RoleService roleService;

    /**
     * 分页查询
     *
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('role.show')")
    @RequestMapping
    public String index(@RequestParam Map filters, Model model) {
        model.addAttribute("filters", filters);
        PageInfo<Role> pageInfo = roleService.findPage(filters);

        model.addAttribute("page", pageInfo);


        return PAGE_INDEX;
    }


    /**
     * 弹出新增页面
     *
     * @return
     */
    @PreAuthorize("hasAnyAuthority('role.create')")
    @RequestMapping("/create")
    public String create() {
        return PAGE_CREATE;
    }

    /**
     * 新增用户
     *
     * @param role
     * @return
     */
    @PreAuthorize("hasAnyAuthority('role.create')")
    @PostMapping("/save")
    public String save(Role role, Model model) {

        roleService.insert(role);

        return successPage(model, "新增用户成功!");
    }

    /**
     * 弹出修改页面
     *
     * @return
     */
    @PreAuthorize("hasAnyAuthority('role.edit')")
    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        //查询用户
        Role role = roleService.getById(id);
        model.addAttribute("role", role);
        return PAGE_EDIT;
    }

    /**
     * 修改角色
     *
     * @param role
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('role.edit')")
    @PostMapping("/update")
    public String update(Role role, Model model) {
        roleService.update(role);
        return successPage(model, "修改用户成功!");
    }

    /**
     * 根据角色id删除角色
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('role.delete')")
    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        roleService.delete(id);
        return LIST_INDEX;
    }

    /**
     * 查看权限列表   zNodes
     *
     * @param roleId 角色id
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('role.assgin')")
    @RequestMapping("/assignShow/{id}")
    public String assignShow(@PathVariable("id") Long roleId, Model model) {
        model.addAttribute("roleId",roleId);

        List<Map<String, Object>> zNodes = roleService.assignShow(roleId);
        model.addAttribute("zNodes", JSON.toJSONString(zNodes));
        return PAGE_ASSIGNSHOW;
    }

    /**
     *
     * @param roleId 角色id
     * @param permissionIds 准备授权的权限ids
     * @param model
     * @return
     */
    @PreAuthorize("hasAnyAuthority('role.assgin')")
    @PostMapping("/assignPermission")
    public String assignPermission(Long roleId,Long[] permissionIds,Model model) {

        roleService.assignPermission(roleId,permissionIds);

        return successPage(model, "修改角色权限成功!");
    }

}
