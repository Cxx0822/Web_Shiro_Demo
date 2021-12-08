package com.shiro.demo.service.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.shiro.demo.service.entity.UserRole;
import com.shiro.demo.service.mapper.UserRoleMapper;
import com.shiro.demo.service.result.R;
import com.shiro.demo.service.service.UserRoleService;
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
@RequestMapping("/user-role")
public class UserRoleController {
    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @ApiOperation("增加userRole信息")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public R createUserRole(UserRole userRole) {
        if(userRole.getRoleId() == null || userRole.getUserId() == 0){
            return R.error().message("请输入userRole信息");
        }

        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id",userRole.getUserId());
        userRoleQueryWrapper.eq("role_id",userRole.getRoleId());

        List<UserRole> UserRoleList = userRoleMapper.selectList(userRoleQueryWrapper);

        if(UserRoleList.size() != 0){
            return R.error().message("userRole已存在");
        }else{
            int insert = userRoleMapper.insert(userRole);
            if (insert != 0) {
                return R.ok().message("增加成功");
            } else {
                return R.error().message("增加失败");
            }
        }
    }

    @ApiOperation("删除userRole信息")
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public R deleteUserRole(Integer userId, Integer roleId) {
        if(userId == null || roleId == null){
            return R.error().message("请输入userRole信息");
        }

        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", userId);
        userRoleQueryWrapper.eq("role_id", roleId);

        List<UserRole> UserRoleList = userRoleMapper.selectList(userRoleQueryWrapper);
        if (UserRoleList.size() == 0){
            return R.error().message("userRole信息不存在");
        }

        int delete = userRoleMapper.delete(userRoleQueryWrapper);

        if (delete != 0) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }
    }

    @ApiOperation("修改userRole信息")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public R updateUserRole(UserRole  userRole) {
        if(userRole.getUserId() == null || userRole.getRoleId() == null){
            return R.error().message("请输入userRole信息");
        }

        UpdateWrapper<UserRole> userRoleUpdateWrapper = new UpdateWrapper<>();
        userRoleUpdateWrapper.eq("user_id", userRole.getUserId());
        userRoleUpdateWrapper.eq("role_id", userRole.getRoleId());

        UserRole userRole1 = userRoleMapper.selectOne(userRoleUpdateWrapper);
        if (userRole1 == null){
            return R.error().message("userRole信息不存在");
        }

        int update = userRoleMapper.update(userRole, userRoleUpdateWrapper);

        if (update != 0) {
            return R.ok().message("修改成功");
        } else {
            return R.error().message("修改失败");
        }
    }

    @ApiOperation("查询userRole信息")
    @RequestMapping(value = "read", method = RequestMethod.GET)
    public R readUserRole(Integer  userId, Integer roleId) {
        if(userId == null || roleId == null){
            return R.error().message("请输入userRole信息");
        }

        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", userId);
        userRoleQueryWrapper.eq("role_id", roleId);

        List<UserRole> UserRoleList = userRoleMapper.selectList(userRoleQueryWrapper);
        if (UserRoleList.size() == 0){
            return R.error().message("userRole信息不存在");
        }else {
            return R.ok().data("data", UserRoleList);
        }
    }
}

