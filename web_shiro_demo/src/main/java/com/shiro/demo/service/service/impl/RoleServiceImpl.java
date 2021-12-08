package com.shiro.demo.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.shiro.demo.service.entity.*;
import com.shiro.demo.service.mapper.*;
import com.shiro.demo.service.result.R;
import com.shiro.demo.service.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Cxx
 * @since 2021-12-06
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<Role> query(String columnName, String columnValue) {
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq(columnName, columnValue);

        return roleMapper.selectList(roleQueryWrapper);
    }

    @Override
    public Boolean delete(String columnName, String columnValue) {
        // 删除角色
        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq(columnName, columnValue);
        Role role = roleMapper.selectOne(roleQueryWrapper);
        int deleteRole = roleMapper.delete(roleQueryWrapper);
        int deletePermission = 0;

        // 删除权限
        QueryWrapper<RolePermission> rolePermissionQueryWrapper = new QueryWrapper<>();
        rolePermissionQueryWrapper.eq("role_id", role.getId());
        List<RolePermission> rolePermissionList = rolePermissionMapper.selectList(rolePermissionQueryWrapper);
        if(rolePermissionList.size() != 0) {
            deletePermission = rolePermissionMapper.delete(rolePermissionQueryWrapper);
        }

        int deleteMenu = 0;
        // 删除资源
        QueryWrapper<RoleMenu> roleMenuQueryWrapper = new QueryWrapper<>();
        roleMenuQueryWrapper.eq("role_id", role.getId());
        List<RoleMenu> roleMenuList = roleMenuMapper.selectList(roleMenuQueryWrapper);
        if(roleMenuList.size() != 0) {
            deleteMenu = roleMenuMapper.delete(roleMenuQueryWrapper);
        }

        if (deleteRole != 0 || deletePermission != 0 || deleteMenu != 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Integer update(Role role) {
        UpdateWrapper<Role> roleUpdateWrapper = new UpdateWrapper<>();
        roleUpdateWrapper.eq("role_name", role.getRoleName());

        return roleMapper.update(role, roleUpdateWrapper);
    }

    @Override
    public Boolean assignRoleMenu(Integer roleId, List<Integer> menuList) {
        QueryWrapper<RoleMenu> roleMenuQueryWrapper = new QueryWrapper<>();
        roleMenuQueryWrapper.select("menu_id").eq("role_id", roleId);
        List<Object> dbRoleMenuList = roleMenuMapper.selectObjs(roleMenuQueryWrapper);

        List<Object> listAll = new ArrayList();
        listAll.addAll(menuList);
        listAll.addAll(dbRoleMenuList);
        LinkedHashSet<Object> newMenuList = new LinkedHashSet<Object>(listAll);

        // 是否添加/删除 成功标志
        int insert = 0, delete = 0;

        for (Object menu : menuList) {
            // 查找该menu的parent_id，如果menu_role没有，则新建
            QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
            menuQueryWrapper.eq("id", menu);
            Menu dbMenu = menuMapper.selectOne(menuQueryWrapper);

            roleMenuQueryWrapper.clear();
            roleMenuQueryWrapper.eq("role_id", roleId);
            roleMenuQueryWrapper.eq("menu_id", dbMenu.getParentId());
            RoleMenu dbRoleMenu = roleMenuMapper.selectOne(roleMenuQueryWrapper);
            if (dbRoleMenu == null) {
                // 插入menu的parent menu
                RoleMenu roleMenuParent = new RoleMenu();
                roleMenuParent.setRoleId(roleId);
                roleMenuParent.setMenuId(dbMenu.getParentId());
                // 插入新的menu
                insert = roleMenuMapper.insert(roleMenuParent);
            }

            // 如果数据库中已有menu，则将合并后的newMenuList中的删除
            if (dbRoleMenuList.contains(menu)) {
                newMenuList.remove(menu);
            } else {
                newMenuList.remove(menu);
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId((Integer) menu);
                // 插入新的menu
                insert = roleMenuMapper.insert(roleMenu);
            }
        }

        // 删除 数据库中存在，但更新的menu中不存在的数据
        for (Object menu2 : newMenuList) {
            roleMenuQueryWrapper.clear();
            roleMenuQueryWrapper.eq("role_id", roleId);
            roleMenuQueryWrapper.eq("menu_id", menu2);
            delete = roleMenuMapper.delete(roleMenuQueryWrapper);
        }

        if (insert == 0 && delete == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Boolean assignRolePermission(Integer roleId, List<Integer> permissionList) {
        QueryWrapper<RolePermission> rolePermissionQueryWrapper = new QueryWrapper<RolePermission>();
        // 查询数据库中role_id的permission_id
        rolePermissionQueryWrapper.select("permission_id").eq("role_id", roleId);
        List<Object> dbRolePermissionList = rolePermissionMapper.selectObjs(rolePermissionQueryWrapper);

        List<Object> listAll = new ArrayList();
        listAll.addAll(permissionList);
        listAll.addAll(dbRolePermissionList);
        // 合并之后的新的 permission_list
        LinkedHashSet<Object> newPermissionList = new LinkedHashSet<Object>(listAll);

        // 是否添加/删除 成功标志
        int insert = 0, delete = 0;

        for (Object permission : permissionList) {
            // 如果permission_id已经在数据库中存在，则将新的permission_list中的该元素删除
            if (dbRolePermissionList.contains(permission)) {
                newPermissionList.remove(permission);
            } else {
                // 如果不存在，则新增，同时也要删除newPermissionList的permission_ids
                newPermissionList.remove(permission);
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(roleId);
                rolePermission.setPermissionId((Integer) permission);
                insert = rolePermissionMapper.insert(rolePermission);
            }
        }

        // 最后根据还剩的新的permission_list中的元素，在数据库中删除
        // 举例：数据库的是[1,2,3]，此时传入[2,3,4]，正确的操作为：添加[4]，[2,3]不变，删除[1]
        // 也就是数据库中只能剩下传入的数据
        for (Object permission2 : newPermissionList) {
            rolePermissionQueryWrapper.clear();
            rolePermissionQueryWrapper.eq("role_id", roleId);
            rolePermissionQueryWrapper.eq("permission_id", permission2);
            delete = rolePermissionMapper.delete(rolePermissionQueryWrapper);
        }

        if (insert == 0 && delete == 0) {
            return false;
        } else {
            return true;
        }
    }
}
