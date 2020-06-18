package com.wm.zgy.bootmybatismbplusshiroesquartz.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: renjiaxin
 * @Despcription:
 * @Date: Created in 2020/6/14 22:50
 * @Modified by:
 */
@RequestMapping("/shiro")
@Controller
@Slf4j
public class ShiroController {

    @RequestMapping(value = {"/index", "/test"})
    @ResponseBody
    public String shiroIndex() {
        return "hello shiro";
    }

    @RequestMapping("addmsg")
    public String add() {
        return "addmsg";
    }

    @RequestMapping("getmsg")
    public String getMsg() {
        return "getmsg";
    }

    @GetMapping(value = "login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String loginCheck(String user, String password) {
        // 简单的测试
        /**
        if (null == user || null == password) {
            return "404";
        } else if ("123456".equals(password)) {
            return "index";
        } else {
            return "login";
        }
         */
        // 获取当前的用户
        Subject subject = SecurityUtils.getSubject();
        // 封装用户登录的数据
        UsernamePasswordToken token = new UsernamePasswordToken(user, password);
        try{
            // 执行登录方法，没有异常说明okay
            subject.login(token);
            return "index";
        }catch (UnknownAccountException e1) {
            log.error("用户名错误！ {} !", e1.getCause());
            return "login";
        }catch (IncorrectCredentialsException e2){
            log.error("密码错误！ {} !", e2.getCause());
            return "login";
        }
    }

    @RequestMapping("unauthor")
    @ResponseBody
    public String unauthor(){
        return "页面未经授权，不得访问！";
    }

    // 权限的检测
    @RequiresPermissions("user:money")
    @GetMapping("getmoney")
    public String getMoney(){
        try{
            // TODO: 问题，如果没有登录，则直接跳到登录页面，如果没有权限，则跳到未授权页面，不应该在这儿再次处理了吧？
            // Subject subject = SecurityUtils.getSubject();
            System.out.println("获取1000元!");
            return "getmoney";
        }catch (AuthorizationException e){
            System.out.println(e.getCause());
            return "login";
        }
    }

    // 角色的检测
    @RequiresRoles("roles:admin")
    @GetMapping("getallinfo")
    public String getAllInfo(){
        try{
            System.out.println("我是管理，想干啥就干啥！");
            return "allinfo";
        }catch (AuthenticationException e){
            System.out.println(e.getCause());
            return "login";
        }
    }
}
