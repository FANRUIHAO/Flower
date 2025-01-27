package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("/shopping")
public class ShoppingController {
    @RequestMapping("/list")
    public String list(){
        return "shopping/list";
    }
}
