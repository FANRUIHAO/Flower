package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/shopping")
public class ShoppingController {
//    @RequestMapping("/list")
//    public String list(){
//        return "shopping/list";
//    }
    @GetMapping("/list")
    public String shoppingList(HttpSession session, Model model) {
        Object user = session.getAttribute("currentUser"); // 获取 Session 用户
        model.addAttribute("username", user != null ? user : "游客"); // 设置默认值
        return "shopping/list"; // 返回商城页面
    }
}
