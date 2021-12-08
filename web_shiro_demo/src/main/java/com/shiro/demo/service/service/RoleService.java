package com.shiro.demo.service.service;

import com.shiro.demo.service.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.demo.service.entity.User;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Cxx
 * @since 2021-12-06
 */
public interface RoleService extends IService<Role> {
    // 查询
    List<Role> query(String columnName, String columnValue);

    Boolean delete(String columnName, String columnValue);

    Integer update(Role role);

    // 分配角色权限
    Boolean assignRoleMenu(Integer roleId, List<Integer> menuList);

    // 分配角色资源
    Boolean assignRolePermission(Integer roleId, List<Integer> permissionList);
}
