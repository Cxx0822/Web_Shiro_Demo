package com.shiro.demo.service.mapper;

import com.shiro.demo.service.entity.MenuMeta;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiro.demo.service.entity.front.FrontMenuMeta;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Cxx
 * @since 2021-12-06
 */
@Repository
public interface MenuMetaMapper extends BaseMapper<MenuMeta> {
    @Select("select * from menu_meta where menu_id=#{menuId}")
    @Results(id = "MetaMap", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "title", property = "title", jdbcType = JdbcType.VARCHAR),
            @Result(column = "icon", property = "icon", jdbcType = JdbcType.VARCHAR),
            @Result(column = "no_cache", property = "noCache", jdbcType = JdbcType.SMALLINT),
            @Result(column = "affix", property = "affix", jdbcType = JdbcType.SMALLINT),
            @Result(column = "breadcrumb", property = "breadcrumb", jdbcType = JdbcType.SMALLINT),
            @Result(column = "active_menu", property = "activeMenu", jdbcType = JdbcType.VARCHAR),
            // 路由角色权限
            @Result(column = "menu_id", property = "roles", many = @Many(select = "com.shiro.demo.service.mapper.RoleMapper.selectRolesByMenuId"))
    })
    FrontMenuMeta selectMetaByMenuId(Integer menuId);
}
