package com.shiro.demo.service.config;

import com.shiro.demo.service.shiro.JwtFilter;
import com.shiro.demo.service.shiro.MyCredentialsMatcher;
import com.shiro.demo.service.shiro.MyRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    // 1.创建shiroFilter
    // 负责拦截所有请求
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager, JwtFilter jwtFilter) {
        // 实例化shiroFilter
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 给filter设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        // 添加自定义过滤器
        Map<String, Filter> filterMap = new HashMap<>();
        filterMap.put("jwt", jwtFilter);
        shiroFilterFactoryBean.setFilters(filterMap);

        // 配置拦截设置
        Map<String, String> map = new LinkedHashMap<>();
        // 不拦截swagger
        // anon为shiro自带的过滤器，即不拦截
        map.put("/swagger-ui.html#/**", "anon");
        map.put("/v2/api-docs", "anon");
        map.put("/swagger-resources/**", "anon");
        map.put("/webjars/**", "anon");

        // 其余均用自定义jwt拦截
        map.put("/**", "jwt");

        //默认认证界面路径---当认证不通过时跳转
        shiroFilterFactoryBean.setLoginUrl("/user/login");
        shiroFilterFactoryBean.setUnauthorizedUrl("/user/login");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);

        return shiroFilterFactoryBean;
    }

    // 自定义过滤器，注入Bean中
    @Bean
    public JwtFilter getJwtFilter() {
        return new JwtFilter();
    }

    //2.创建安全管理器
    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(MyRealm myRealm, MyCredentialsMatcher myCredentialsMatcher) {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        // 设置Realm的自定义密码验证器
        myRealm.setCredentialsMatcher(myCredentialsMatcher);
        // 设置自定义Realm
        defaultWebSecurityManager.setRealm(myRealm);

        return defaultWebSecurityManager;
    }

    // 3.自定义realm 注入Bean中
    @Bean
    public MyRealm getRealm() {

        return new MyRealm();
    }

    // 自定义密码验证器 注入Bean中
    @Bean
    public MyCredentialsMatcher getMyCredentialsMatcher() {

        return new MyCredentialsMatcher();
    }

    /**
     *  开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),
     *  需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
     *  配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    /**
     * 开启aop注解支持
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
