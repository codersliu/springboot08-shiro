package com.sliu.shirospringboot.config;

import com.sliu.shirospringboot.pojo.User;
import com.sliu.shirospringboot.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

//自定义的UserRealm，继承AuthorizingRealm
public class UserRealm extends AuthorizingRealm {
    @Autowired
    UserService userService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了授权======>doGetAuthorizationInfo");
        //SimpleAuthorizationInfo
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //获取当前登陆的对象Subject
        Subject subject = SecurityUtils.getSubject();
        User currentUser = (User) subject.getPrincipal();

        //获取当前用户权限
        info.addStringPermission(currentUser.getPerm());

        //返回info
        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("执行了认证======>doGetAuthenticationInfo");

        //用户名，密码 从数据库中取
//        String name = "root";
//        String password = "123456";

        UsernamePasswordToken userToken = (UsernamePasswordToken) token;
//        if(!userToken.getUsername().equals(name)){
//            return null; //抛出异常 UnkonwnAccountException
//        }

        //密码认证，shiro实现
//        return new SimpleAuthenticationInfo("",password,"");
        //真实数据库获取账号密码并核验正确性
        User user = userService.queryUserByName(userToken.getUsername());
        //用户不存在
        if(user==null){
            return null;//UnknownAccountException
        }

        Subject currentSubject = SecurityUtils.getSubject();
        Session session = currentSubject.getSession();
        session.setAttribute("loginUser", user);

        //支持加密
        //密码认证，code无明文密码
        return new SimpleAuthenticationInfo(user, user.getPassword(),"");
    }
}
