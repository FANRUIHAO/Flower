package com.controller;

import com.entity.Product;
import com.github.pagehelper.PageInfo;
import com.service.ProductService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Properties;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum,
                       @RequestParam(defaultValue = "10") int pageSize,
                       Model model) {
        PageInfo<Product> pageInfo = productService.selectProduct(pageNum, pageSize);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("products", pageInfo.getList());
        return "product/list";
    }
//    public String list(Model model){
//        List<Product> list=productService.selectProduct();
//        model.addAttribute("products", list);
//        return "product/list";
//    }
    @RequestMapping("/delete")
    public String delete(@RequestParam Integer id){
        productService.deleteProduct(id);

        return "redirect:/product/list";
    }

}
