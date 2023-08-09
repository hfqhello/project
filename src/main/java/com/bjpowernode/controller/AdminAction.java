package com.bjpowernode.controller;


import com.bjpowernode.pojo.Admin;
import com.bjpowernode.service.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminAction {
        @Autowired
        AdminService adminService;
        //实现登录判断 并进行相应的跳转
        @RequestMapping("/login")
        public String login(String name, String pwd, HttpServletRequest  request){
            Admin admin=adminService.login(name,pwd);
            System.out.println("......................");
            if(admin!=null){
                //登录成功
                System.out.println(".........222.............");
                request.setAttribute("admin",admin);
                return  "main";
            }else {
                request.setAttribute("errmsg","用户名密码不正确!!");
                System.out.println(".........3333333333.............");
                return  "login";
            }
        }
}
