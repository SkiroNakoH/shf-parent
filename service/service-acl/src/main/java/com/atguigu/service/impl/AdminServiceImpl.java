package com.atguigu.service.impl;

import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Admin;
import com.atguigu.entity.AdminRole;
import com.atguigu.entity.Permission;
import com.atguigu.entity.Role;
import com.atguigu.helper.PermissionHelper;
import com.atguigu.mapper.AdminMapper;
import com.atguigu.mapper.AdminRoleMapper;
import com.atguigu.mapper.RoleMapper;
import com.atguigu.service.AdminService;

import org.apache.dubbo.config.annotation.DubboService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DubboService
public class AdminServiceImpl extends BaseServiceImpl<Admin> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public BaseMapper<Admin> getBaseMapper() {
        return adminMapper;
    }

    /**
     * 根据用户id查询角色列表,包含assignRoleList 和 unAssignRoleList
     *
     * @param adminId
     * @return
     */
    @Override
    public Map<String, List<Role>> findMapByAdminId(Long adminId) {
        //根据用户id，查询授权的角色ids
        List<Long> assignRoleIdList = adminRoleMapper.findRoleIdByAdminId(adminId);

        //获取所有role
        List<Role> roleList = roleMapper.findAll();
        //存储已授权的role
        List<Role> assignRoleList = new ArrayList<>();
        //存储未授权的role
        List<Role> unAssignRoleList = new ArrayList<>();

        for (Role role : roleList) {
            if (assignRoleIdList.contains(role.getId())) {
                //已授权
                assignRoleList.add(role);
            } else {
                //未授权
                unAssignRoleList.add(role);
            }
        }

        Map<String, List<Role>> map = new HashMap<>();
        map.put("assignRoleList", assignRoleList);
        map.put("unAssignRoleList", unAssignRoleList);

        return map;
    }

    @Override
    public void assignRole(Long adminId, List<Long> assignRoleIdList) {
        //从中间表中通过用户id删除角色
        adminRoleMapper.deleteByAdminId(adminId);

        //新增
        for (Long assignRoleId : assignRoleIdList) {
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(assignRoleId);

            adminRoleMapper.insert(adminRole);
        }
    }


    /**
     * 根据用户查找对应的权限列表
     *
     * @param adminId
     * @return
     */
    @Override
    public List<Permission> findPermissionListByAdminId(Long adminId) {
        List<Permission> permissionList = adminRoleMapper.findPermissionListByAdminId(adminId);

        return PermissionHelper.build(permissionList);
    }

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    @Override
    public Admin getByName(String username) {
        return adminMapper.getByName(username);
    }
}
