package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.entity.Role;

import java.util.List;
import java.util.Map;

public interface AdminService extends BaseService<Admin> {
    Map<String, List<Role>> findMapByAdminId(Long adminId);

    void assignRole(Long adminId, List<Long> assignRoleIdList);

    List<Permission> findPermissionListByAdminId(Long adminId);

    Admin getByName(String username);
}
