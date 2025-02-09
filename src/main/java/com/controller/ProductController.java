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
                       @RequestParam(required = false) String keyword,
                       Model model) {
        PageInfo<Product> pageInfo = productService.selectProduct(keyword,pageNum, pageSize);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("products", pageInfo.getList());
        return "product/list";
    }
//    public String list(Model model){
//        List<Product> list=productService.selectProduct();
//        model.addAttribute("products", list);
//        return "product/list";
//    }
    @RequestMapping("/add")
    public String addProduct() {
        return "product/add"; // 返回添加商品的页面（如 add.html 或模态框内容）
    }
    @RequestMapping("/save")//添加操作
    public String save(Product p){
        productService.saveProduct(p);
        return "redirect:/product/list";
    }
    @RequestMapping("/edit")
    public String edit(@RequestParam Integer id, Model model){
        Product p=productService.selectProductById(id);
        model.addAttribute("p", p);
        return "product/edit";
    }
    @RequestMapping("/delete")
    public String delete(@RequestParam Integer id){
        productService.deleteProduct(id);

        return "redirect:/product/list";
    }

}










