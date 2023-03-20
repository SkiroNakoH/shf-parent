package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Permission;


import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {
    List<Permission> findListByParentId(Long parentId);

    List<String> findCodeListByAdminId(Long adminId);
}
