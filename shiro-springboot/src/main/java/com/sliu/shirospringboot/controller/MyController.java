package com.sliu.shirospringboot.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyController {

    @RequestMapping({"/", "/index", "/index.html"})
    public String toIndex(Model model){
        model.addAttribute("msg", "hello Shiro!");
        return "index";
    }

    @RequestMapping("/user/add")
    public String toAdd(){
        return "/user/add";
    }

    @RequestMapping("/user/update")
    public String toUpdate(){
        return "/user/update";
    }

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "/login";
    }

    @RequestMapping("/login")
    public String login(String username, String password, Model model){
        //获取当前用户
        Subject subject = SecurityUtils.getSubject();

        //封装用户登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        //执行登陆方法，没有异常就ok了
        try {
            subject.login(token);
            return "index";
        }catch (UnknownAccountException e){//用户名不存在
            model.addAttribute("msg", "用户名错误");
            return "login";
        }catch (IncorrectCredentialsException e){
            model.addAttribute("msg", "密码错误");
            return "login";
        }

    }

    @RequestMapping("/unauthor")
    @ResponseBody
    public String unAuthour(){
        return "当前用户未授权，无法访问指定页面";
    }
}
