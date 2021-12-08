package com.shiro.demo.service.mapper;

import com.shiro.demo.service.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiro.demo.service.entity.front.FrontRole;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Cxx
 * @since 2021-12-06
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {
    // 获取所有角色信息
    @Select("select * from role")
    @Results(id = "RoleMap", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "role_name", property = "roleName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "role_description", property = "roleDescription", jdbcType = JdbcType.VARCHAR),
            // 角色的menu信息
            @Result(column = "id", property = "routes", many = @Many(select = "com.shiro.demo.service.mapper.MenuMapper.selectMenuByRoleId")),
            // 角色的权限信息
            @Result(column = "id", property = "permissionList", many = @Many(select = "com.shiro.demo.service.mapper.PermissionMapper.selectPermissionByRoleId"))
    })
    List<FrontRole> getAllRoles();

    @Select({"select *",
            "from role rl, user_role ur",
            "where ur.id=#{id} and ur.role_id=rl.id"})
    @Results(id = "RoleSimpleMap", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "role_name", property = "roleName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "role_description", property = "roleDescription", jdbcType = JdbcType.VARCHAR)
    })
        // 根据用户ID查询角色信息
    List<FrontRole> selectRolesByUserId(Integer userId);

    // 选择menu_role表中所有menu_id为meta_id的role_name值
    // 即可以选择某个menu的角色权限
    @Select({"select rl.role_name",
            "from role rl, role_menu rm",
            "where rm.menu_id=#{metaId} and rm.role_id=rl.id"})
    // 注：待解决bug：为什么这里要写成 {} 的形式，不能写成上文的selectRolesByUserId()的形式？？
    List<String> selectRolesByMenuId(Integer metaId);

    // 根据permission获取能访问的角色信息
    @Select({"select rl.role_name",
            "from role rl, role_permission rp, permission pm",
            "where pm.permission_name=#{permissionName} and pm.id=rp.permission_id and rp.role_id=rl.id"})
    List<String> getRoleNameByPermissionName(String permissionName);
}
