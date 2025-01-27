package com.controller;

import com.entity.Product;
import com.service.ProductService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Properties;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @RequestMapping("/list")
    public String list(Model model){
        List<Product> list=productService.selectProduct();
        model.addAttribute("products", list);
        return "product/list";
    }
//    @RequestMapping("/add")
//    public String add(){
//        return "product/add";
//    }


}
