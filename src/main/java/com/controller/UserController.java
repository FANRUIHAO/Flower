package com.controller;

import com.entity.User;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
    @RequestMapping("/delete")
    public String delete(@RequestParam Integer id){
        userService.deleteUser(id);

        return "redirect:/user/list";
    }
    @RequestMapping("/edit")
    public String edit(@RequestParam Integer id,Model model){
        User u=userService.selectUserById(id);
        model.addAttribute("u",u);
        return "user/add";
    }
    @RequestMapping("/save")
    public String save(User u){
        System.out.println("1");
        userService.saveUser(u);
        return "redirect:/user/list";
    }



}
