package com.shiro.demo.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shiro.demo.service.entity.User;
import com.shiro.demo.service.entity.front.FrontUser;
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
public interface UserMapper extends BaseMapper<User> {
    // 获取所有用户信息
    @Select("select * from user")
    @Results(id = "UserMap", value = {
            @Result(column = "id", property = "id", jdbcType = JdbcType.INTEGER, id = true),
            @Result(column = "username", property = "username", jdbcType = JdbcType.VARCHAR),
            @Result(column = "nickname", property = "nickname", jdbcType = JdbcType.VARCHAR),
            @Result(column = "introduction", property = "introduction", jdbcType = JdbcType.VARCHAR),
            @Result(column = "avatar", property = "avatar", jdbcType = JdbcType.VARCHAR),
            // 用户的角色信息
            @Result(column = "id", property = "roleList", many = @Many(select = "com.shiro.demo.service.mapper.RoleMapper.selectRolesByUserId"))
    })
    List<FrontUser> getAllUsers();
}
