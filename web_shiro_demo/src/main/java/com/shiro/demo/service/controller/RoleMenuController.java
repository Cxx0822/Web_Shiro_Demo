package com.shiro.demo.service.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.shiro.demo.service.entity.RoleMenu;
import com.shiro.demo.service.mapper.RoleMenuMapper;
import com.shiro.demo.service.result.R;
import com.shiro.demo.service.service.RoleMenuService;
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
@RequestMapping("/role-menu")
public class RoleMenuController {
    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private RoleMenuMapper roleMenuMapper;
    
    @ApiOperation("增加roleMenu信息")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public R createRoleMenu(RoleMenu roleMenu) {
        if(roleMenu.getRoleId() == null || roleMenu.getMenuId() == 0){
            return R.error().message("请输入roleMenu信息");
        }

        QueryWrapper<RoleMenu> roleMenuQueryWrapper = new QueryWrapper<>();
        roleMenuQueryWrapper.eq("role_id",roleMenu.getRoleId());
        roleMenuQueryWrapper.eq("menu_id",roleMenu.getMenuId());

        List<RoleMenu> roleMenuList = roleMenuMapper.selectList(roleMenuQueryWrapper);

        if(roleMenuList.size() != 0){
            return R.error().message("roleMenu已存在");
        }else{
            int insert = roleMenuMapper.insert(roleMenu);
            if (insert != 0) {
                return R.ok().message("增加成功");
            } else {
                return R.error().message("增加失败");
            }
        }
    }

    @ApiOperation("删除roleMenu信息")
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public R deleteRoleMenu(Integer roleId, Integer menuId) {
        if(roleId == null || menuId == null){
            return R.error().message("请输入roleMenu信息");
        }

        QueryWrapper<RoleMenu> roleMenuQueryWrapper = new QueryWrapper<>();
        roleMenuQueryWrapper.eq("role_id", roleId);
        roleMenuQueryWrapper.eq("menu_id", menuId);

        List<RoleMenu> roleMenuList = roleMenuMapper.selectList(roleMenuQueryWrapper);
        if (roleMenuList.size() == 0){
            return R.error().message("roleMenu信息不存在");
        }

        int delete = roleMenuMapper.delete(roleMenuQueryWrapper);

        if (delete != 0) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }
    }

    @ApiOperation("修改roleMenu信息")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public R updateRoleMenu(RoleMenu  roleMenu) {
        if(roleMenu.getRoleId() == null || roleMenu.getMenuId() == null){
            return R.error().message("请输入roleMenu信息");
        }

        UpdateWrapper<RoleMenu> roleMenuUpdateWrapper = new UpdateWrapper<>();
        roleMenuUpdateWrapper.eq("role_id", roleMenu.getRoleId());
        roleMenuUpdateWrapper.eq("menu_id", roleMenu.getMenuId());

        RoleMenu rolePermission1 = roleMenuMapper.selectOne(roleMenuUpdateWrapper);
        if (rolePermission1 == null){
            return R.error().message("roleMenu信息不存在");
        }

        int update = roleMenuMapper.update(roleMenu, roleMenuUpdateWrapper);

        if (update != 0) {
            return R.ok().message("修改成功");
        } else {
            return R.error().message("修改失败");
        }
    }

    @ApiOperation("查询roleMenu信息")
    @RequestMapping(value = "read", method = RequestMethod.GET)
    public R readRoleMenu(Integer roleId, Integer menuId) {
        if(roleId == null || menuId == null){
            return R.error().message("请输入roleMenu信息");
        }

        QueryWrapper<RoleMenu> roleMenuQueryWrapper = new QueryWrapper<>();
        roleMenuQueryWrapper.eq("role_id", roleId);
        roleMenuQueryWrapper.eq("menu_id", menuId);

        List<RoleMenu> roleMenuList = roleMenuMapper.selectList(roleMenuQueryWrapper);
        if (roleMenuList.size() == 0){
            return R.error().message("roleMenu信息不存在");
        }else {
            return R.ok().data("data", roleMenuList);
        }
    }
}

