package com.shiro.demo.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.shiro.demo.service.entity.Role;
import com.shiro.demo.service.entity.User;
import com.shiro.demo.service.entity.UserRole;
import com.shiro.demo.service.entity.front.FrontMenu;
import com.shiro.demo.service.entity.front.FrontUser;
import com.shiro.demo.service.mapper.MenuMapper;
import com.shiro.demo.service.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.demo.service.mapper.UserRoleMapper;
import com.shiro.demo.service.service.MenuService;
import com.shiro.demo.service.service.RoleService;
import com.shiro.demo.service.service.UserRoleService;
import com.shiro.demo.service.service.UserService;
import com.shiro.demo.service.shiro.JwtToken;
import com.shiro.demo.service.utils.JwtUtil;
import com.shiro.demo.service.utils.SaltUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Cxx
 * @since 2021-12-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public Integer create(User user) {
        // 1.生成 n位 随机盐
        String salt = SaltUtil.getSalt(8);
        // 2.将随机盐保存到数据
        user.setSalt(salt);
        // 3.明文密码进行 md5 + salt + hash散列n次 加密
        Md5Hash md5Hash = new Md5Hash(user.getPassword(), salt, 1024);

        // 转换为字符串数据
        user.setPassword(md5Hash.toHex());

        // 插入数据库中
        return userMapper.insert(user);
    }

    @Override
    public List<User> query(String columnName, String columnValue) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq(columnName, columnValue);

        return userMapper.selectList(userQueryWrapper);
    }

    @Override
    public Boolean delete(String columnName, String columnValue) {
        // 删除用户
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq(columnName, columnValue);
        User user = userMapper.selectOne(userQueryWrapper);
        int deleteUser = userMapper.delete(userQueryWrapper);
        int deleteRole = 0;

        // 删除角色
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<>();
        userRoleQueryWrapper.eq("user_id", user.getId());
        UserRole userRole = userRoleMapper.selectOne(userRoleQueryWrapper);
        if(userRole != null) {
            deleteRole = userRoleMapper.delete(userRoleQueryWrapper);
        }

        if (deleteUser != 0 || deleteRole != 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Integer update(User user, User dbUser) {
        // 如果修改了密码，需要重新加密
        if (user.getPassword() != null && user.getPassword().length() > 0) {
            String newPassword;
            // 判断用户是否输入盐值
            if (user.getSalt() == null || user.getSalt().length() == 0) {
                newPassword = new Md5Hash(user.getPassword(), dbUser.getSalt(), 1024).toHex();
            } else {
                newPassword = new Md5Hash(user.getPassword(), user.getSalt(), 1024).toHex();
            }

            user.setPassword(newPassword);
        }

        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("username", user.getUsername());
        return userMapper.update(user, userUpdateWrapper);
    }

    @Override
    public String login(User user) {
        // 生成 shiro 主题subject
        Subject subject = SecurityUtils.getSubject();
        // 根据用户名 issuer subject生成token
        String token = JwtUtil.createJWT(user.getUsername(), "back", "user");
        // 根据token和密码 生成自定义JwtToken
        JwtToken jwtToken = new JwtToken(token, user.getPassword());

        // shiro登录login
        subject.login(jwtToken);

        return token;
    }

    @Override
    public HashMap<String, Object> getRoleInfo(String username) {
        // 根据用户名获取用户信息
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        User user = userMapper.selectOne(userQueryWrapper);

        // 返回数据格式
        List<String> roles = new ArrayList<String>();
        HashMap<String, Object> userRoleInfoMap = new HashMap<>();
        // 需要返回的前端用户信息
        userRoleInfoMap.put("username", user.getUsername());
        userRoleInfoMap.put("nickname", user.getNickname());
        userRoleInfoMap.put("introduction", user.getIntroduction());
        userRoleInfoMap.put("avatar", user.getAvatar());

        // 用户角色信息
        // 根据用户ID查询用户角色ID
        UserRole userRole = userRoleService.getById(user.getId());
        if (userRole == null) {
            // 没有找到角色ID则返回空数据 不能返回null或者error!
            roles.add("");
        } else {
            // 根据角色ID查询角色信息
            Role role = roleService.getById(userRole.getRoleId());
            if (role == null) {
                roles.add("");
            }else {
                roles.add(role.getRoleName());
            }
        }
        userRoleInfoMap.put("roles", roles);

        return userRoleInfoMap;
    }


    @Override
    public Boolean assignUserRole(User user, Role role) {
        // 根据用户ID更新 用户-角色 信息
        UpdateWrapper<UserRole> userRoleUpdateWrapper = new UpdateWrapper<>();
        userRoleUpdateWrapper.eq("user_id", user.getId());

        UserRole userRole = userRoleMapper.selectOne(userRoleUpdateWrapper);
        // 如果用户-角色不存在 则新建
        int insert = 0, update = 0;
        if (userRole == null) {
            UserRole newUserRole = new UserRole();
            newUserRole.setUserId(user.getId());
            newUserRole.setRoleId(role.getId());
            insert = userRoleMapper.insert(newUserRole);
        } else {
            userRole.setRoleId(role.getId());
            update = userRoleMapper.update(userRole, userRoleUpdateWrapper);
        }

        if (insert != 0 || update != 0) {
            return true;
        } else {
            return false;
        }
    }
}
