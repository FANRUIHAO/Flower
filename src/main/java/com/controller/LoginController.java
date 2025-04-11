package com.controller;

import com.entity.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductController productController;
//    @PostMapping("/login")
//    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
//        User user = userService.login(username, password);
//        if (user != null) {
//            session.setAttribute("currentUser", user);
//            // 登录成功后跳转到 /product/firstlist
//            return "redirect:/product/firstlist";
//        } else {
//            return "redirect:/user/login?error=true";
//        }
//    }
@PostMapping("/login")
public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
    User user = userService.login(username, password);
    if (user != null) {
        session.setAttribute("currentUser", user);
        // 重定向到 /product/firstlist，触发 listProducts 方法
        return "redirect:/product/firstlist";
    } else {
        return "redirect:/user/login?error=true";
    }
}



}
