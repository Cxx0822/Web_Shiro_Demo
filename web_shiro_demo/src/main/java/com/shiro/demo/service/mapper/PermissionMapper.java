package com.shiro.demo.service.mapper;

import com.shiro.demo.service.entity.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiro.demo.service.entity.front.FrontPermission;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Cxx
 * @since 2021-12-06
 */
@Repository
public interface PermissionMapper extends BaseMapper<Permission> {
    // 根据角色选择权限
    @Select("select pm.* " +
            "from permission pm, role_permission rp " +
            "where rp.role_id=#{roleId} and pm.id=rp.permission_id")
    @Results(id = "PermissionMap", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "permission_name", property = "permissionName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "permission_description", property = "permissionDescription", jdbcType = JdbcType.VARCHAR),
            @Result(column = "resource_id", property = "resourceId", jdbcType = JdbcType.INTEGER),
            @Result(column = "operation_id", property = "operationId", jdbcType = JdbcType.INTEGER),
            // 操作信息
            @Result(column = "operation_id", property = "operation", one = @One(select = "com.shiro.demo.service.mapper.OperationMapper.selectOperationByOperationID")),
            // 资源信息
            @Result(column = "resource_id", property = "resource", one = @One(select = "com.shiro.demo.service.mapper.ResourceMapper.selectResourceByResourceId"))
    })
    List<FrontPermission> selectPermissionByRoleId(Integer roleId);

    @Select("select * from permission where resource_id=#{resourceId}")
    @ResultMap("PermissionMap")
    List<FrontPermission> selectPermissionByResourceId(Integer resourceId);
}
