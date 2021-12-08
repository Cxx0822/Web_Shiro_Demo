package com.shiro.demo.service.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.shiro.demo.service.entity.Permission;
import com.shiro.demo.service.mapper.PermissionMapper;
import com.shiro.demo.service.result.R;
import com.shiro.demo.service.service.PermissionService;
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
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PermissionMapper permissionMapper;

    @ApiOperation("增加权限信息")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public R createPermission(Permission permission) {
        if(permission.getPermissionName() == null || permission.getPermissionName().length() == 0){
            return R.error().message("请输入权限信息");
        }

        QueryWrapper<Permission> permissionQueryWrapper = new QueryWrapper<>();
        permissionQueryWrapper.eq("permission_name",permission.getPermissionName());

        List<Permission> permissionList = permissionMapper.selectList(permissionQueryWrapper);

        if(permissionList.size() != 0){
            return R.error().message("权限已存在");
        }else{
            int insert = permissionMapper.insert(permission);
            if (insert != 0) {
                return R.ok().message("增加成功");
            } else {
                return R.error().message("增加失败");
            }
        }
    }

    @ApiOperation("删除权限信息")
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public R deletePermission(String  permissionName) {
        if(permissionName == null || permissionName.length() == 0){
            return R.error().message("请输入权限信息");
        }

        QueryWrapper<Permission> permissionQueryWrapper = new QueryWrapper<>();
        permissionQueryWrapper.eq("permission_name",permissionName);

        List<Permission> permissionList = permissionMapper.selectList(permissionQueryWrapper);
        if (permissionList.size() == 0){
            return R.error().message("权限信息不存在");
        }

        int delete = permissionMapper.delete(permissionQueryWrapper);

        if (delete != 0) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }
    }

    @ApiOperation("修改权限信息")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public R updatePermission(Permission  permission) {
        if(permission.getPermissionName() == null || permission.getPermissionName().length() == 0){
            return R.error().message("请输入权限信息");
        }

        UpdateWrapper<Permission> permissionUpdateWrapper = new UpdateWrapper<>();
        permissionUpdateWrapper.eq("permission_name", permission.getPermissionName());

        Permission permission1 = permissionMapper.selectOne(permissionUpdateWrapper);
        if (permission1 == null){
            return R.error().message("权限信息不存在");
        }

        int update = permissionMapper.update(permission, permissionUpdateWrapper);

        if (update != 0) {
            return R.ok().message("修改成功");
        } else {
            return R.error().message("修改失败");
        }
    }

    @ApiOperation("查询权限信息")
    @RequestMapping(value = "read", method = RequestMethod.GET)
    public R readPermission(String  permissionName) {
        if(permissionName == null || permissionName.length() == 0){
            return R.error().message("请输入权限信息");
        }

        QueryWrapper<Permission> permissionQueryWrapper = new QueryWrapper<>();
        permissionQueryWrapper.eq("permission_name", permissionName);

        List<Permission> permissionList = permissionMapper.selectList(permissionQueryWrapper);
        if (permissionList.size() == 0){
            return R.error().message("权限信息不存在");
        }else {
            return R.ok().data("data", permissionList);
        }
    }
}

