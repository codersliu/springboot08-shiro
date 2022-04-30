package com.sliu.shirospringboot.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        //设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);

        //添加shiro内置过滤器
        /*
        anon: 无需认证即可访问
        authc: 必须认证才能访问
        user: 必须配置记住我 功能才能用
        perms: 拥有对某个资源的权限才能访问
        role: 拥有某个角色权限才能访问
         */

        Map<String, String> filterMap = new LinkedHashMap<>();
        //设置授权，正常情况下，未授权跳转到未授权页面
        //只有拥有user:add权限的用户才能访问/user/add资源
        filterMap.put("/user/add", "perms[user:add]");

        filterMap.put("/user/update", "perms[user:update]");

        //设置user下的资源必须认证才能访问
        filterMap.put("/user/*", "authc");

        bean.setFilterChainDefinitionMap(filterMap);

        //设置认证失败，登陆请求
        bean.setLoginUrl("/toLogin");

        //设置未授权跳转页面
        bean.setUnauthorizedUrl("/unauthor");
        return bean;
    }

    //DefaultWebSecurityManager
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联UserRealm
        securityManager.setRealm(userRealm());
        return securityManager;
    }

    //创建realm对象，需要自定义类
    @Bean(name = "userRealm")
    public UserRealm userRealm(){
        return new UserRealm();
    }

    //整合ShiroDialect 用来整合Shiro Thymeleaf
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }
}
