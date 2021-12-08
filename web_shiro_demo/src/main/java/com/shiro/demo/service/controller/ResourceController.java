package com.shiro.demo.service.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.shiro.demo.service.entity.Resource;
import com.shiro.demo.service.entity.front.FrontResource;
import com.shiro.demo.service.mapper.ResourceMapper;
import com.shiro.demo.service.result.R;
import com.shiro.demo.service.service.ResourceService;
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
@RequestMapping("/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ResourceMapper resourceMapper;

    @ApiOperation("增加资源信息")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public R createResource(Resource resource) {
        if(resource.getResourceName() == null || resource.getResourceName().length() == 0){
            return R.error().message("请输入资源信息");
        }

        QueryWrapper<Resource> resourceQueryWrapper = new QueryWrapper<>();
        resourceQueryWrapper.eq("resource_name",resource.getResourceName());

        List<Resource> resourceList = resourceMapper.selectList(resourceQueryWrapper);

        if(resourceList.size() != 0){
            return R.error().message("资源已存在");
        }else{
            int insert = resourceMapper.insert(resource);
            if (insert != 0) {
                return R.ok().message("增加成功");
            } else {
                return R.error().message("增加失败");
            }
        }
    }

    @ApiOperation("删除资源信息")
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public R deleteResource(String  resourceName) {
        if(resourceName == null || resourceName.length() == 0){
            return R.error().message("请输入资源信息");
        }

        QueryWrapper<Resource> resourceQueryWrapper = new QueryWrapper<>();
        resourceQueryWrapper.eq("resource_name",resourceName);

        List<Resource> resourceList = resourceMapper.selectList(resourceQueryWrapper);
        if (resourceList.size() == 0){
            return R.error().message("资源信息不存在");
        }

        int delete = resourceMapper.delete(resourceQueryWrapper);

        if (delete != 0) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }
    }

    @ApiOperation("修改资源信息")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public R updateResource(Resource  resource) {
        if(resource.getResourceName() == null){
            return R.error().message("请输入资源信息");
        }

        UpdateWrapper<Resource> resourceUpdateWrapper = new UpdateWrapper<>();
        resourceUpdateWrapper.eq("resource_name", resource.getResourceName());

        Resource resource1 = resourceMapper.selectOne(resourceUpdateWrapper);
        if (resource1 == null){
            return R.error().message("资源信息不存在");
        }

        int update = resourceMapper.update(resource, resourceUpdateWrapper);

        if (update != 0) {
            return R.ok().message("修改成功");
        } else {
            return R.error().message("修改失败");
        }
    }

    @ApiOperation("查询资源信息")
    @RequestMapping(value = "read", method = RequestMethod.GET)
    public R readResource(String  resourceName) {
        if(resourceName == null || resourceName.length() == 0){
            return R.error().message("请输入资源信息");
        }

        QueryWrapper<Resource> resourceQueryWrapper = new QueryWrapper<>();
        resourceQueryWrapper.eq("resource_name", resourceName);

        List<Resource> resourceList = resourceMapper.selectList(resourceQueryWrapper);
        if (resourceList.size() == 0){
            return R.error().message("资源信息不存在");
        }else {
            return R.ok().data("data", resourceList);
        }
    }

    @ApiOperation("查询所有资源信息")
    @RequestMapping(value = "allResources", method = RequestMethod.GET)
    public R getAllResources(){
        List<FrontResource> frontResourceList = resourceMapper.getAllResources();
        return R.ok().data("data", frontResourceList);
    }
}

