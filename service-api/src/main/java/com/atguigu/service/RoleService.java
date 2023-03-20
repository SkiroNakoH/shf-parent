package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface RoleService extends BaseService<Role> {

    List<Map<String, Object>> assignShow(Long roleId);

    void assignPermission(Long roleId, Long[] permissionIds);
}
