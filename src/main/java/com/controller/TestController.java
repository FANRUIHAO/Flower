package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

@RequestMapping("/test1")
public class TestController {
    @RequestMapping("/index")
    public String dosomething(String username, String password){
        System.out.println("111");
        System.out.println(username);
        System.out.println(password);
        return "index";
    }
}
