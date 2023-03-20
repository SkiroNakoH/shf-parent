package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Permission;

import java.util.List;

public interface PermissionService extends BaseService<Permission> {
    List<Permission> findPermissionHelperAll();

    List<String> findCodeListByAdminId(Long adminId);
}
