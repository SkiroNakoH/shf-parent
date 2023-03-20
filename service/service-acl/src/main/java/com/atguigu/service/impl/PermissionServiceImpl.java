package com.atguigu.service.impl;

import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Permission;
import com.atguigu.helper.PermissionHelper;
import com.atguigu.mapper.PermissionMapper;
import com.atguigu.service.PermissionService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public BaseMapper<Permission> getBaseMapper() {
        return permissionMapper;
    }


    @Override
    public List<Permission> findPermissionHelperAll() {
        List<Permission> permissionList = permissionMapper.findAll();

        return PermissionHelper.build(permissionList);
    }

    @Override
    public List<String> findCodeListByAdminId(Long adminId) {
        return permissionMapper.findCodeListByAdminId(adminId);
    }
}
