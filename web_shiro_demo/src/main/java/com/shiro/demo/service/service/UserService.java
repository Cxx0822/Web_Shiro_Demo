package com.shiro.demo.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shiro.demo.service.entity.Role;
import com.shiro.demo.service.entity.User;
import com.shiro.demo.service.entity.front.FrontMenu;
import com.shiro.demo.service.entity.front.FrontUser;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Cxx
 * @since 2021-12-06
 */
public interface UserService extends IService<User> {
    // 注册用户
    Integer create(User user);

    List<User> query(String columnName, String columnValue);

    // 删除用户及其角色信息
    Boolean delete(String columnName, String columnValue);

    Integer update(User user, User dbUser);

    String login(User user);

    // 获取单个用户的角色信息
    HashMap<String, Object> getRoleInfo(String username);


    // 分配用户角色
    Boolean assignUserRole(User user, Role role);
}
