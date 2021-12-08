package com.shiro.demo.service.mapper;

import com.shiro.demo.service.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiro.demo.service.entity.front.FrontMenu;
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
public interface MenuMapper extends BaseMapper<Menu> {
    // 获取所有路由信息
    @Select("select * from menu where parent_id is null or parent_id=0")
    @Results(id = "MenuMap", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "path", property = "path", jdbcType = JdbcType.VARCHAR),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "component", property = "component", jdbcType = JdbcType.VARCHAR),
            @Result(column = "redirect", property = "redirect", jdbcType = JdbcType.VARCHAR),
            @Result(column = "hidden", property = "hidden", jdbcType = JdbcType.SMALLINT),
            @Result(column = "always_show", property = "alwaysShow", jdbcType = JdbcType.SMALLINT),
            // meta信息
            @Result(column = "id", property = "meta", one = @One(select = "com.shiro.demo.service.mapper.MenuMetaMapper.selectMetaByMenuId")),
            // children信息
            @Result(column = "id", property = "children", many = @Many(select = "com.shiro.demo.service.mapper.MenuMapper.selectAllMenusByParentId"))
    })
    List<FrontMenu> getAllMenus();

    @Select("select * from menu where parent_id = #{parentId}")
    @ResultMap("MenuMap")
    // 根据parent_id查询路由信息
    List<FrontMenu> selectAllMenusByParentId(Integer parentId);

    // 根据角色选择menu
    @Select("select mn.* " +
            "from menu mn, role_menu rm " +
            "where rm.role_id=#{roleId} and mn.id=rm.menu_id")
    @Results(id = "MenuSimpleMap", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "path", property = "path", jdbcType = JdbcType.VARCHAR),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "component", property = "component", jdbcType = JdbcType.VARCHAR),
            @Result(column = "redirect", property = "redirect", jdbcType = JdbcType.VARCHAR),
            @Result(column = "hidden", property = "hidden", jdbcType = JdbcType.SMALLINT),
            @Result(column = "always_show", property = "alwaysShow", jdbcType = JdbcType.SMALLINT),
            @Result(column = "parent_id", property = "parentId", jdbcType = JdbcType.INTEGER)
    })
    List<FrontMenu> selectMenuByRoleId(Integer roleId);
}
