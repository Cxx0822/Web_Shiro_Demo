package com.shiro.demo.service.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shiro.demo.service.entity.Role;
import com.shiro.demo.service.entity.User;
import com.shiro.demo.service.entity.UserRole;
import com.shiro.demo.service.mapper.RoleMapper;
import com.shiro.demo.service.mapper.UserMapper;
import com.shiro.demo.service.mapper.UserRoleMapper;
import com.shiro.demo.service.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


// 自定义Realm
@Repository
public class MyRealm extends AuthorizingRealm {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    // 使用自定义token
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    // 授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取用户名
        String username = (String) principalCollection.getPrimaryPrincipal();

        // 根据用户名获取当前用户的角色信息，以及权限信息
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        // 数据库中获取用户的角色信息
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        User user = userMapper.selectOne(userQueryWrapper);

        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", user.getId());
        UserRole userRole = userRoleMapper.selectOne(userRoleQueryWrapper);

        QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
        roleQueryWrapper.eq("id", userRole.getRoleId());
        Role role = roleMapper.selectOne(roleQueryWrapper);

        // 添加角色 权限信息
        // admin
        simpleAuthorizationInfo.addRole(role.getRoleName());
        simpleAuthorizationInfo.addStringPermission("user:*");

        return simpleAuthorizationInfo;
    }

    // 验证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) authenticationToken;
        // Jwt获取username
        String token = (String) jwtToken.getPrincipal();    // 拿到token
        Claims claims = JwtUtil.parseJWT(token);            // 解析jwt
        String username = claims.getId();                   // jwt中拿到username

        // 数据库获取用户信息并验证
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);

        User user = userMapper.selectOne(userQueryWrapper);

        if(user == null){
            return null;
        }

        // 返回密码对比结果 这里会保存用户的用户名和密码，以便后续的密码验证
        return new SimpleAuthenticationInfo(username, user.getPassword(), getName());
    }
}


