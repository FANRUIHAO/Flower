package com.controller;

import com.entity.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping("/list")
    public String  list(Model model){
        List<User> list=userService.selectUser();
        model.addAttribute("users",list);

        return "user/list";
    }
    @RequestMapping("/add")
    public String add(){
        return "user/add";
    }
}
