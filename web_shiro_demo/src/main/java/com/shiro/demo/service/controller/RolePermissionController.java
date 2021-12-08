package com.shiro.demo.service.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.shiro.demo.service.entity.RolePermission;
import com.shiro.demo.service.mapper.RolePermissionMapper;
import com.shiro.demo.service.result.R;
import com.shiro.demo.service.service.RolePermissionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Cxx
 * @since 2021-12-06
 */
@RestController
@RequestMapping("/role-permission")
public class RolePermissionController {
    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @ApiOperation("增加rolePermission信息")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public R createRolePermission(RolePermission rolePermission) {
        if(rolePermission.getRoleId() == null || rolePermission.getPermissionId() == 0){
            return R.error().message("请输入rolePermission信息");
        }

        QueryWrapper<RolePermission> rolePermissionQueryWrapper = new QueryWrapper<>();
        rolePermissionQueryWrapper.eq("role_id",rolePermission.getRoleId());
        rolePermissionQueryWrapper.eq("permission_id",rolePermission.getPermissionId());

        List<RolePermission> RolePermissionList = rolePermissionMapper.selectList(rolePermissionQueryWrapper);

        if(RolePermissionList.size() != 0){
            return R.error().message("rolePermission已存在");
        }else{
            int insert = rolePermissionMapper.insert(rolePermission);
            if (insert != 0) {
                return R.ok().message("增加成功");
            } else {
                return R.error().message("增加失败");
            }
        }
    }

    @ApiOperation("删除rolePermission信息")
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public R deleteRolePermission(Integer roleId, Integer permissionId) {
        if(roleId == null || permissionId == null){
            return R.error().message("请输入rolePermission信息");
        }

        QueryWrapper<RolePermission> rolePermissionQueryWrapper = new QueryWrapper<>();
        rolePermissionQueryWrapper.eq("role_id", roleId);
        rolePermissionQueryWrapper.eq("permission_id", permissionId);

        List<RolePermission> RolePermissionList = rolePermissionMapper.selectList(rolePermissionQueryWrapper);
        if (RolePermissionList.size() == 0){
            return R.error().message("rolePermission信息不存在");
        }

        int delete = rolePermissionMapper.delete(rolePermissionQueryWrapper);

        if (delete != 0) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }
    }

    @ApiOperation("修改rolePermission信息")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public R updateRolePermission(RolePermission  rolePermission) {
        if(rolePermission.getRoleId() == null || rolePermission.getPermissionId() == null){
            return R.error().message("请输入rolePermission信息");
        }

        UpdateWrapper<RolePermission> rolePermissionUpdateWrapper = new UpdateWrapper<>();
        rolePermissionUpdateWrapper.eq("role_id", rolePermission.getRoleId());
        rolePermissionUpdateWrapper.eq("permission_id", rolePermission.getPermissionId());

        RolePermission rolePermission1 = rolePermissionMapper.selectOne(rolePermissionUpdateWrapper);
        if (rolePermission1 == null){
            return R.error().message("rolePermission信息不存在");
        }

        int update = rolePermissionMapper.update(rolePermission, rolePermissionUpdateWrapper);

        if (update != 0) {
            return R.ok().message("修改成功");
        } else {
            return R.error().message("修改失败");
        }
    }

    @ApiOperation("查询rolePermission信息")
    @RequestMapping(value = "read", method = RequestMethod.GET)
    public R readRolePermission(Integer roleId, Integer permissionId) {
        if(roleId == null || permissionId == null){
            return R.error().message("请输入rolePermission信息");
        }

        QueryWrapper<RolePermission> rolePermissionQueryWrapper = new QueryWrapper<>();
        rolePermissionQueryWrapper.eq("role_id", roleId);
        rolePermissionQueryWrapper.eq("permission_id", permissionId);

        List<RolePermission> RolePermissionList = rolePermissionMapper.selectList(rolePermissionQueryWrapper);
        if (RolePermissionList.size() == 0){
            return R.error().message("rolePermission信息不存在");
        }else {
            return R.ok().data("data", RolePermissionList);
        }
    }
}

