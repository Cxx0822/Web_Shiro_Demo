package com.shiro.demo.service.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.shiro.demo.service.entity.Operation;
import com.shiro.demo.service.mapper.OperationMapper;
import com.shiro.demo.service.result.R;
import com.shiro.demo.service.service.OperationService;
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
@RequestMapping("/operation")
public class OperationController {
    @Autowired
    private OperationService operationService;

    @Autowired
    private OperationMapper operationMapper;

    @ApiOperation("增加操作信息")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public R createOperation(Operation operation) {
        if(operation.getOperationName() == null || operation.getOperationName().length() == 0){
            return R.error().message("请输入操作信息");
        }

        QueryWrapper<Operation> operationQueryWrapper = new QueryWrapper<>();
        operationQueryWrapper.eq("operation_name",operation.getOperationName());

        List<Operation> operationList = operationMapper.selectList(operationQueryWrapper);

        if(operationList.size() != 0){
            return R.error().message("操作已存在");
        }else{
            int insert = operationMapper.insert(operation);
            if (insert != 0) {
                return R.ok().message("增加成功");
            } else {
                return R.error().message("增加失败");
            }
        }
    }

    @ApiOperation("删除操作信息")
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public R deleteOperation(String  operation_name) {
        if(operation_name == null || operation_name.length() == 0){
            return R.error().message("请输入操作信息");
        }

        QueryWrapper<Operation> operationQueryWrapper = new QueryWrapper<>();
        operationQueryWrapper.eq("operation_name",operation_name);

        List<Operation> operationList = operationMapper.selectList(operationQueryWrapper);
        if (operationList.size() == 0){
            return R.error().message("操作信息不存在");
        }

        int delete = operationMapper.delete(operationQueryWrapper);

        if (delete != 0) {
            return R.ok().message("删除成功");
        } else {
            return R.error().message("删除失败");
        }
    }

    @ApiOperation("修改操作信息")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public R updateOperation(Operation  operation) {
        if(operation.getOperationName() == null){
            return R.error().message("请输入操作信息");
        }

        UpdateWrapper<Operation> operationUpdateWrapper = new UpdateWrapper<>();
        operationUpdateWrapper.eq("operation_name", operation.getOperationName());

        Operation operation1 = operationMapper.selectOne(operationUpdateWrapper);
        if (operation1 == null){
            return R.error().message("操作信息不存在");
        }



        int update = operationMapper.update(operation, operationUpdateWrapper);

        if (update != 0) {
            return R.ok().message("修改成功");
        } else {
            return R.error().message("修改失败");
        }
    }

    @ApiOperation("查询操作信息")
    @RequestMapping(value = "read", method = RequestMethod.GET)
    public R readOperation(String  operationName) {
        if(operationName == null || operationName.length() == 0){
            return R.error().message("请输入操作信息");
        }

        QueryWrapper<Operation> operationQueryWrapper = new QueryWrapper<>();
        operationQueryWrapper.eq("operation_name", operationName);

        List<Operation> operationList = operationMapper.selectList(operationQueryWrapper);
        if (operationList.size() == 0){
            return R.error().message("操作信息不存在");
        }else {
            return R.ok().data("data", operationList);
        }
    }
}

