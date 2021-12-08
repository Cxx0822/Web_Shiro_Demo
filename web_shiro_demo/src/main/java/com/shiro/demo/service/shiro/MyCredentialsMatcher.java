package com.shiro.demo.service.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shiro.demo.service.entity.User;
import com.shiro.demo.service.mapper.UserMapper;
import com.shiro.demo.service.service.UserService;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// 自定义密码验证器
@Component
public class MyCredentialsMatcher extends SimpleCredentialsMatcher {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        JwtToken jwtToken = (JwtToken) token;

        if(jwtToken.getPassword() == null) {
            return true;
        }

        // 输入密码
        String inputPassword = new String(jwtToken.getPassword());

        // 数据库密码
        String username = String.valueOf(info.getPrincipals());
        String dbPassword = (String) info.getCredentials();

        // 数据库盐值
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);

        User user = userMapper.selectOne(userQueryWrapper);
        String salt = user.getSalt();

        // 加密后的密码  md5+盐值+迭代次数
        String md5Password = new SimpleHash("md5", inputPassword, salt, 1024).toHex();

        // 比较密码
        return this.equals(md5Password, dbPassword);
    }
}
