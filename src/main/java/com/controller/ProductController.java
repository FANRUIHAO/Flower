package com.controller;

import com.entity.Cart;
import com.entity.Comment;
import com.entity.Product;
import com.entity.User;
import com.github.pagehelper.PageInfo;
import com.service.CartService;
import com.service.CommentsService;
import com.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;
    @Autowired
    private CommentsService commentService;
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
    String uploadDir = new File("target/classes/static/images/").getAbsolutePath();
    File uploadDirFile = new File(uploadDir);
    @RequestMapping("/save")
    public String save(Product p, @RequestParam("image") MultipartFile image) {
        if (!image.isEmpty()) {
            try {
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
    @GetMapping("/firstlist")
    public String listProducts(HttpSession session, Model model, @RequestParam(value = "user_image", required = false, defaultValue = "/static/images/person/p1.jpg") String user_image) {
        User u = (User) session.getAttribute("currentUser");
        if (u != null) {
            // 如果用户已登录，显示用户信息
            model.addAttribute("username", u.getUsername());
            model.addAttribute("userImage", u.getUser_image());
            // 根据用户等级判断是否显示后台管理按钮
            if (u.getGrade() == User.Grade.ADMIN) {
                model.addAttribute("showAdminButton", true);
            } else {
                model.addAttribute("showAdminButton", false);
            }
        } else {
            // 如果未登录，显示游客欢迎信息
            model.addAttribute("username", "游客");
            model.addAttribute("showAdminButton", false);
        }
        // 获取按 star 排序的前 3 个商品
        List<Product> topProducts = productService.getTop3ProductsByStar();
        model.addAttribute("products", topProducts);
        return "shopping/list";
    }
    @PostMapping("/addToCart")
    @ResponseBody
    public Map<String, Object> addToCartFromHome(@RequestParam String productName,
                                    @RequestParam Double productPrice,
                                    @RequestParam String productImage,
                                    @RequestParam Integer quantity,
                                    HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            response.put("status", "redirect");
            response.put("url", "/user/login");
            return response;
        }

        if (productPrice == null || productPrice <= 0) {
            response.put("status", "error");
            response.put("message", "Invalid product price!");
            return response;
        }

        if (quantity == null || quantity <= 0) {
            response.put("status", "error");
            response.put("message", "Quantity must be greater than 0!");
            return response;
        }

        try {
            Cart existingItem = cartService.findByUserIdAndProductName(user.getId(), productName);

            if (existingItem != null) {
                int newQuantity = existingItem.getCnum() + quantity;
                existingItem.setCnum(newQuantity);
                cartService.update(existingItem);
                response.put("message", "Product quantity updated in cart!");
            } else {
                Cart cartItem = new Cart();
                cartItem.setUser_id(user.getId());
                cartItem.setCname(productName);
                cartItem.setCprice(productPrice);
                cartItem.setImage_url(productImage);
                cartItem.setCnum(quantity);
                cartService.save(cartItem);
                response.put("message", "Product added to cart successfully!");
            }

            response.put("status", "success");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Failed to add product to cart: " + e.getMessage());
        }

        return response;
    }

    @RequestMapping("/comments")
    public String listComments(@RequestParam(defaultValue = "1") int pageNum,
                               @RequestParam(defaultValue = "10") int pageSize,
                               Model model) {
        System.out.println("111");
        PageInfo<Comment> pageInfo = commentService.getComments(pageNum, pageSize);


        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("comments", pageInfo.getList());

        return "product/comment";
    }

}










