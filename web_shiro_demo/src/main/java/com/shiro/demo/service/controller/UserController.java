package com.shiro.demo.service.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.shiro.demo.service.entity.Role;
import com.shiro.demo.service.entity.User;
import com.shiro.demo.service.entity.UserRole;
import com.shiro.demo.service.entity.front.FrontMenu;
import com.shiro.demo.service.entity.front.FrontUser;
import com.shiro.demo.service.mapper.MenuMapper;
import com.shiro.demo.service.mapper.RoleMapper;
import com.shiro.demo.service.mapper.UserMapper;
import com.shiro.demo.service.mapper.UserRoleMapper;
import com.shiro.demo.service.result.R;
import com.shiro.demo.service.service.RoleService;
import com.shiro.demo.service.service.UserRoleService;
import com.shiro.demo.service.service.UserService;
import com.shiro.demo.service.shiro.JwtToken;
import com.shiro.demo.service.utils.JwtUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Cxx
 * @since 2021-12-06
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private MenuMapper menuMapper;

    @ApiOperation("增加用户信息")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public R createUser(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().length() == 0
                || user.getPassword() == null || user.getPassword().length() == 0) {
            return R.error().message("请输入用户名和密码");
        }

        List<User> userList = userService.query("username", user.getUsername());

        if (userList.size() != 0) {
            return R.error().message("用户已存在");
        } else {
            // 增加用户
            Integer result = userService.create(user);
            if (result != 0) {
                return R.ok().message("增加成功");
            } else {
                return R.error().message("增加失败");
            }
        }
    }

    @ApiOperation("删除用户信息")
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public R deleteUser(String username) {
        if (username == null || username.length() == 0) {
            return R.error().message("请输入用户名");
        }

        List<User> userList = userService.query("username", username);

        if (userList.size() == 0) {
            return R.error().message("用户不存在");
        } else {
            // 删除用户和角色
            Boolean result = userService.delete("username", username);

            if (result) {
                return R.ok().message("删除成功");
            } else {
                return R.error().message("删除失败");
            }
        }
    }

    @ApiOperation("修改用户信息")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public R updateUser(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().length() == 0) {
            return R.error().message("请输入用户名");
        }

        List<User> userList = userService.query("username", user.getUsername());
        if (userList.size() == 0) {
            return R.error().message("用户不存在");
        }

        // 数据库中的User信息
        User dbUser = userList.get(0);
        // 更新用户信息
        Integer update = userService.update(user, dbUser);

        if (update != 0) {
            return R.ok().message("修改成功");
        } else {
            return R.error().message("修改失败");
        }
    }

    @ApiOperation("查询用户信息")
    @RequestMapping(value = "read", method = RequestMethod.GET)
    public R readUser(String username) {
        if (username == null || username.length() == 0) {
            return R.error().message("请输入用户名");
        }

        List<User> userList = userService.query("username", username);
        if (userList.size() == 0) {
            return R.error().message("用户不存在");
        } else {
            return R.ok().data("data", userList);
        }
    }

    @ApiOperation("登录")
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public R login(@RequestBody User user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            return R.error().message("账号密码不能为空");
        }

        String token = userService.login(user);

        // 向前端返回token字符串
        return R.ok().data("token", token);
    }

    @ApiOperation("退出登录")
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public R logOut(@RequestHeader("X-Token") String token) {
        SecurityUtils.getSubject().logout();
        return R.ok().message("退出登录成功");
    }

    @ApiOperation("获取用户角色信息")
    @RequestMapping(value = "roleInfo", method = RequestMethod.GET)
    // 这里需要token参数，此时前端还不知道用户的信息，只有token信息
    public R getRoleInfo(@RequestHeader("X-Token") String token) {
        // 根据token信息获取username
        String username = JwtUtil.parseJWT(token).getId();
        if (username == null) {
            return R.error().message("未登陆");
        } else {
            // 获取用户的角色信息
            HashMap<String, Object> userRoleInfoMap = userService.getRoleInfo(username);
            return R.ok().data("data", userRoleInfoMap);
        }
    }

    @ApiOperation("获取所有用户角色信息")
    @RequestMapping(value = "allRoleInfo", method = RequestMethod.GET)
    public R getAllRoleInfo() {
        List<FrontUser> allUsers = userMapper.getAllUsers();
        return R.ok().data("data", allUsers);
    }

    @ApiOperation("获取所有资源/路由信息")
    @RequestMapping(value = "routes", method = RequestMethod.GET)
    public R getMenu() {
        List<FrontMenu> menus = menuMapper.getAllMenus();

        return R.ok().data("data", menus);
    }

    @ApiOperation("分配用户角色信息")
    @RequestMapping(value = "assignUserRole", method = RequestMethod.POST)
    public R assignUserRole(String username, String roleName){

        List<User> userList = userService.query("username", username);
        if (userList.size() == 0) {
            return R.error().message("用户不存在");
        }

        List<Role> roleList = roleService.query("role_name", roleName);
        if(roleList.size() == 0) {
            return R.error().message("角色不存在");
        }

        // 分配用户角色
        Boolean result = userService.assignUserRole(userList.get(0), roleList.get(0));

        if (result) {
            return R.ok().message("分配角色成功");
        } else {
            return R.error().message("分配角色失败");
        }
    }
}

