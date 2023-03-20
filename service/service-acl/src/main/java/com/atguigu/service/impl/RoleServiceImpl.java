package com.atguigu.service.impl;

import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Permission;
import com.atguigu.entity.Role;
import com.atguigu.entity.RolePermission;
import com.atguigu.mapper.PermissionMapper;
import com.atguigu.mapper.RoleMapper;
import com.atguigu.mapper.RolePermissionMapper;
import com.atguigu.service.RoleService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DubboService
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public BaseMapper<Role> getBaseMapper() {
        return roleMapper;
    }

    /**
     * 根据角色id查看权限
     *
     * @param roleId
     * @return
     */
    @Override
    public List<Map<String, Object>> assignShow(Long roleId) {
        List<Permission> allPermissionList = permissionMapper.findAll();

        List<Long> assionPermissionIdList = rolePermissionMapper.findPerMissionIdByRoleId(roleId);

        List<Map<String, Object>> zNodes = new ArrayList<>();

        for (Permission permission : allPermissionList) {
            //map格式未zTree需要的k-v
            Map<String, Object> map = new HashMap<>();
            map.put("id", permission.getId());
            map.put("pId", permission.getParentId());
            map.put("name", permission.getName());
            map.put("checked", assionPermissionIdList.contains(permission.getId()));
            map.put("isParent", permissionMapper.findListByParentId(permission.getId()).size() > 0);
            map.put("open", permissionMapper.findListByParentId(permission.getId()).size() > 0);

            zNodes.add(map);
        }

        return zNodes;
    }

    /**
     * 为roleId授权permissionIds
     * @param roleId 角色id
     * @param permissionIds 准备授权的权限ids
     */
    @Override
    public void assignPermission(Long roleId, Long[] permissionIds) {
        //删除以前授权记录
        rolePermissionMapper.deleteByRoleId(roleId);

        //授权
        for (Long permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);

            rolePermissionMapper.insert(rolePermission);
        }
    }
}
