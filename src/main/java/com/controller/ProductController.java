package com.controller;

import com.entity.Product;
import com.github.pagehelper.PageInfo;
import com.service.ProductService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
        PageInfo<Product> pageInfo = productService.selectProduct(keyword, pageNum, pageSize);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("products", pageInfo.getList());
        return "product/list";
    }
    String uploadDir = "src/main/resources/static/images/";
    File uploadDirFile = new File(uploadDir);
    @RequestMapping("/save")
    public String save(Product p, @RequestParam("image") MultipartFile image) {
        if (!image.isEmpty()) {
            try {
                // 设定上传目录为项目的静态资源路径
//                String uploadDir = "src/main/resources/static/images"; // 这里不需要使用绝对路径，Spring Boot会自动识别静态资源目录
//                File uploadDirFile = new File(uploadDir);

                // 如果目录不存在，创建目录
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }

                // 生成目标文件路径
                String filePath = uploadDir + File.separator + image.getOriginalFilename();
                File dest = new File(filePath);

                // 保存文件
                image.transferTo(dest);

                // 设置商品的图片路径（将路径存入数据库）
                p.setPro_image("/images/" + image.getOriginalFilename());
                System.out.println(p.getPro_image());
            } catch (IOException e) {
                e.printStackTrace();
                // 处理文件上传错误
                return "redirect:/product/list?error=upload";
            }
        }

        // 保存商品
        productService.saveProduct(p);

        // 跳转到商品列表页面
        return "redirect:/product/list";
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam Integer id, Model model){
        Product p=productService.selectProductById(id);
        model.addAttribute("p", p);
        return "product/edit";
    }

    @RequestMapping("/update")
    public String update(Product p, @RequestParam("image") MultipartFile image) {
        if (!image.isEmpty()) {
            try {
                // 设置文件存储目录
                String uploadDir = new File("target/classes/static/images/").getAbsolutePath();
                File uploadDirFile = new File(uploadDir);
                // 如果目录不存在，创建目录
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }
                // 设置文件存储路径
                String filePath = uploadDir + File.separator + image.getOriginalFilename();
                File dest = new File(filePath);
                // 将文件写入目标路径
                image.transferTo(dest);
                // 更新商品的图片路径
                p.setPro_image("/images/" + image.getOriginalFilename() + "?t=" + System.currentTimeMillis());
            } catch (IOException e) {
                e.printStackTrace();
                // 处理文文件上传错误
                return "redirect:/product/list?error=upload";
            }
        } else {
            // 如果没有上传新图片，保留原来的图片
            Product existingProduct = productService.selectProductById(p.getId());
            p.setPro_image(existingProduct.getPro_image());
        }

        // 更新商品
        productService.updateProduct(p);

        // 跳转到商品列表页面
        return "redirect:/product/list";
    }
    @RequestMapping("/delete")
    public String delete(@RequestParam Integer id){
        productService.deleteProduct(id);

        return "redirect:/product/list";
    }

}










