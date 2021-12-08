package com.shiro.demo.service.mapper;

import com.shiro.demo.service.entity.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiro.demo.service.entity.front.FrontResource;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
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
public interface ResourceMapper extends BaseMapper<Resource> {
    // 根据资源ID获取资源信息
    @Select("select * from resource where id=#{resourceId}")
    @Results(id = "ResourcesSimpleMap", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "resource_name", property = "resourceName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "resource_description", property = "resourceDescription", jdbcType = JdbcType.VARCHAR)
    })
    FrontResource selectResourceByResourceId(Integer resourceId);

    // 查询所有资源
    @Select("select * from resource")
    @Results(id = "ResourcesMap", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "resource_name", property = "resourceName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "resource_description", property = "resourceDescription", jdbcType = JdbcType.VARCHAR),

            @Result(column = "id", property = "permissionList", many = @Many(select = "com.shiro.demo.service.mapper.PermissionMapper.selectPermissionByResourceId"))
    })
    List<FrontResource> getAllResources();
}
