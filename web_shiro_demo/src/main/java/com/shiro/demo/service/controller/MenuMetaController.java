package com.shiro.demo.service.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.shiro.demo.service.entity.MenuMeta;
import com.shiro.demo.service.mapper.MenuMetaMapper;
import com.shiro.demo.service.result.R;
import com.shiro.demo.service.service.MenuMetaService;
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
@RequestMapping("/menu-meta")
public class MenuMetaController {
    @Autowired
    private MenuMetaService menuMetaService;

    @Autowired
    private MenuMetaMapper menuMetaMapper;

    @ApiOperation("增加menuMeta信息")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public R createMenuMeta(MenuMeta menuMeta) {
        if(menuMeta.getTitle() == null || menuMeta.getTitle().length() == 0){
            return R.error().message("请输入menuMeta信息");
        }

        QueryWrapper<MenuMeta> menuMetaQueryWrapper = new QueryWrapper<>();
        menuMetaQueryWrapper.eq("title",menuMeta.getTitle());

        List<MenuMeta> menuMetaList = menuMetaMapper.selectList(menuMetaQueryWrapper);

        if(menuMetaList.size() != 0){
            return R.error().message("menuMeta已存在");
        }else{
            int insert = menuMetaMapper.insert(menuMeta);
            if (insert != 0) {
                return R.ok().message("增加成功");
            } else {
                return R.error().message("增加失败");
            }
        }
    }

    @ApiOperation("删除menuMeta信息")
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public R deleteMenuMeta(String  title) {
        if(title == null || title.length() == 0){
            return R.error().message("请输入menuMeta信息");
        }

        QueryWrapper<MenuMeta> menuMetaQueryWrapper = new QueryWrapper<>();
        menuMetaQueryWrapper.eq("title",title);

        List<MenuMeta> menuMetaList = menuMetaMapper.selectList(menuMetaQueryWrapper);
        if (menuMetaList.size() == 0){
            return R.error().message("menuMeta信息不存在");
        }

        int delete = menuMetaMapper.delete(menuMetaQueryWrapper);

        if (delete != 0) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }
    }

    @ApiOperation("修改menuMeta信息")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public R updateMenuMeta(MenuMeta  menuMeta) {
        if(menuMeta.getTitle() == null || menuMeta.getTitle().length() == 0){
            return R.error().message("请输入menuMeta信息");
        }

        UpdateWrapper<MenuMeta> menuMetaUpdateWrapper = new UpdateWrapper<>();
        menuMetaUpdateWrapper.eq("title", menuMeta.getTitle());

        MenuMeta menuMeta1 = menuMetaMapper.selectOne(menuMetaUpdateWrapper);
        if (menuMeta1 == null){
            return R.error().message("menuMeta信息不存在");
        }

        int update = menuMetaMapper.update(menuMeta, menuMetaUpdateWrapper);

        if (update != 0) {
            return R.ok().message("修改成功");
        } else {
            return R.error().message("修改失败");
        }
    }

    @ApiOperation("查询menuMeta信息")
    @RequestMapping(value = "read", method = RequestMethod.GET)
    public R readMenuMeta(String  title) {
        if(title == null || title.length() == 0){
            return R.error().message("请输入menuMeta信息");
        }

        QueryWrapper<MenuMeta> menuMetaQueryWrapper = new QueryWrapper<>();
        menuMetaQueryWrapper.eq("title", title);

        List<MenuMeta> menuMetaList = menuMetaMapper.selectList(menuMetaQueryWrapper);
        if (menuMetaList.size() == 0){
            return R.error().message("menuMeta信息不存在");
        }else {
            return R.ok().data("data", menuMetaList);
        }
    }
}

