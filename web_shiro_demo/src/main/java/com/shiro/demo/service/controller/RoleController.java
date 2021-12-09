package com.shiro.demo.service.controller;


import com.shiro.demo.service.entity.Role;
import com.shiro.demo.service.entity.front.FrontRole;
import com.shiro.demo.service.mapper.RoleMapper;
import com.shiro.demo.service.result.R;
import com.shiro.demo.service.service.RoleMenuService;
import com.shiro.demo.service.service.RolePermissionService;
import com.shiro.demo.service.service.RoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Cxx
 * @since 2021-12-06
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @ApiOperation("增加角色信息")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public R createRole(@RequestBody Role role) {
        if (role.getRoleName() == null || role.getRoleName().length() == 0) {
            return R.error().message("请输入角色名称");
        }

        List<Role> roleList = roleService.query("role_name", role.getRoleName());

        if (roleList.size() != 0) {
            return R.error().message("角色已存在");
        } else {
            int insert = roleMapper.insert(role);
            if (insert != 0) {
                return R.ok().message("增加成功");
            } else {
                return R.error().message("增加失败");
            }
        }
    }

    @ApiOperation("删除角色信息")
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public R deleteRole(String roleName) {
        if (roleName == null || roleName.length() == 0) {
            return R.error().message("请输入角色名称");
        }

        List<Role> roleList = roleService.query("role_name", roleName);
        if (roleList.size() == 0) {
            return R.error().message("角色不存在");
        }

        Boolean resullt = roleService.delete("role_name", roleName);

        if (resullt) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }
    }

    @ApiOperation("修改角色信息")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public R updateRole(@RequestBody Role role) {
        if (role.getRoleName() == null || role.getRoleName().length() == 0) {
            return R.error().message("请输入角色名称");
        }

        List<Role> roleList = roleService.query("role_name", role.getRoleName());
        if (roleList.size() == 0) {
            return R.error().message("角色不存在");
        }

        int update = roleService.update(role);

        if (update != 0) {
            return R.ok().message("修改成功");
        } else {
            return R.error().message("修改失败");
        }
    }

    @ApiOperation("查询角色信息")
    @RequestMapping(value = "read", method = RequestMethod.GET)
    public R readRole(String roleName) {
        if (roleName == null || roleName.length() == 0) {
            return R.error().message("请输入角色名称");
        }

        List<Role> roleList = roleService.query("role_name", roleName);
        if (roleList.size() == 0) {
            return R.error().message("角色不存在");
        } else {
            return R.ok().data("data", roleList);
        }
    }

    @ApiOperation("查询所有角色信息")
    @RequestMapping(value = "allRoles", method = RequestMethod.GET)
    public R getAllRoles() {
        List<FrontRole> frontRoleList = roleMapper.getAllRoles();

        return R.ok().data("data", frontRoleList);
    }

    @ApiOperation("获取可以访问某个permission的所有角色信息")
    // 即能够访问这个permission的所有角色
    @RequestMapping(value = "getRoleNameByPermissionName", method = RequestMethod.GET)
    public R getRoleNameByPermissionName(String permissionName) {
        List<String> permissionNameList = roleMapper.getRoleNameByPermissionName(permissionName);

        return R.ok().data("data", permissionNameList);
    }

    @ApiOperation("分配角色资源")
    @RequestMapping(value = "assignRoleMenu", method = RequestMethod.POST)
    public R assignRoleMenu(Integer roleId, @RequestParam List<Integer> menuList) {
        // 判断前端用户是否选择资源
        if (menuList.contains(-1)) {
            Integer delete = roleMenuService.delete("role_id", String.valueOf(roleId));
            if (delete != 0) {
                return R.ok().message("分配成功");
            } else {
                return R.error().message("和数据库数据一致或分配失败");
            }
        }else {
            Boolean result = roleService.assignRoleMenu(roleId, menuList);

            if (result) {
                return R.ok().message("分配成功");
            } else {
                return R.error().message("和数据库数据一致或分配失败");
            }
        }
    }

    @ApiOperation("分配角色权限")
    @RequestMapping(value = "assignRolePermission", method = RequestMethod.POST)
    // 权限包括 资源和操作
    public R assignRolePermission(Integer roleId, @RequestParam List<Integer> permissionList) {
        // 判断前端用户是否选择资源
        if (permissionList.contains(-1)) {
            Integer delete = rolePermissionService.delete("role_id", String.valueOf(roleId));
            if (delete != 0) {
                return R.ok().message("分配成功");
            } else {
                return R.error().message("和数据库数据一致或分配失败");
            }
        }else {
            Boolean result = roleService.assignRolePermission(roleId, permissionList);

            if (result) {
                return R.ok().message("分配成功");
            } else {
                return R.error().message("和数据库数据一致或分配失败");
            }
        }

    }
}

