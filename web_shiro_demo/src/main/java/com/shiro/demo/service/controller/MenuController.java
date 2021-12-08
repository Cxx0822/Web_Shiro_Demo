package com.shiro.demo.service.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.shiro.demo.service.entity.Menu;
import com.shiro.demo.service.mapper.MenuMapper;
import com.shiro.demo.service.result.R;
import com.shiro.demo.service.service.MenuService;
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
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuMapper menuMapper;

    @ApiOperation("增加menu信息")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public R createMenu(Menu menu) {
        if(menu.getPath() == null || menu.getPath().length() == 0){
            return R.error().message("请输入menu信息");
        }

        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.eq("path",menu.getPath());

        List<Menu> menuList = menuMapper.selectList(menuQueryWrapper);

        if(menuList.size() != 0){
            return R.error().message("menu已存在");
        }else{
            int insert = menuMapper.insert(menu);
            if (insert != 0) {
                return R.ok().message("增加成功");
            } else {
                return R.error().message("增加失败");
            }
        }
    }

    @ApiOperation("删除menu信息")
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public R deleteMenu(String  path) {
        if(path == null || path.length() == 0){
            return R.error().message("请输入menu信息");
        }

        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.eq("path",path);

        List<Menu> menuList = menuMapper.selectList(menuQueryWrapper);
        if (menuList.size() == 0){
            return R.error().message("menu信息不存在");
        }

        int delete = menuMapper.delete(menuQueryWrapper);

        if (delete != 0) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }
    }

    @ApiOperation("修改menu信息")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public R updateMenu(Menu  menu) {
        if(menu.getPath() == null || menu.getPath().length() == 0){
            return R.error().message("请输入menu信息");
        }

        UpdateWrapper<Menu> menuUpdateWrapper = new UpdateWrapper<>();
        menuUpdateWrapper.eq("path", menu.getPath());

        Menu menu1 = menuMapper.selectOne(menuUpdateWrapper);
        if (menu1 == null){
            return R.error().message("menu信息不存在");
        }

        int update = menuMapper.update(menu, menuUpdateWrapper);

        if (update != 0) {
            return R.ok().message("修改成功");
        } else {
            return R.error().message("修改失败");
        }
    }

    @ApiOperation("查询menu信息")
    @RequestMapping(value = "read", method = RequestMethod.GET)
    public R readMenu(String  path) {
        if(path == null || path.length() == 0){
            return R.error().message("请输入menu信息");
        }

        QueryWrapper<Menu> menuQueryWrapper = new QueryWrapper<>();
        menuQueryWrapper.eq("path", path);

        List<Menu> menuList = menuMapper.selectList(menuQueryWrapper);
        if (menuList.size() == 0){
            return R.error().message("menu信息不存在");
        }else {
            return R.ok().data("data", menuList);
        }
    }
}

