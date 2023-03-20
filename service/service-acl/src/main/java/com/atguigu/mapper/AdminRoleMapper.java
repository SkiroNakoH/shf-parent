package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.AdminRole;
import com.atguigu.entity.Permission;

import java.util.List;

public interface AdminRoleMapper extends BaseMapper<AdminRole> {
    List<Long> findRoleIdByAdminId(Long adminId);

    void deleteByAdminId(Long adminId);

    List<Permission> findPermissionListByAdminId(Long adminId);
}
