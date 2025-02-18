package com.controller;

import com.entity.Cart;
import com.entity.User;
import com.service.CartService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/shopping")
public class ShoppingController {
    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;
    @GetMapping("/")
    public String home() {
        return "redirect:/shopping/list"; // 根路径跳转到购物列表页面
    }
    @PostMapping("/addToCart")
    @ResponseBody
    public Map<String, String> addToCart(@RequestParam String productName, @RequestParam String productPrice, @RequestParam String productImage, @RequestParam Integer quantity, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        Map<String, String> response = new HashMap<>();
        if (user == null) {
            response.put("status", "redirect");
            response.put("url", "/user/login");
            return response;

        }

        Cart cart = new Cart();
        cart.setUser_id(user.getId());
        cart.setCname(productName);
        cart.setCprice(new BigDecimal(productPrice).intValue());
        cart.setImage_url(productImage);
        cart.setCnum(quantity); // 设置数量
        cartService.save(cart);

        response.put("status", "success");
        response.put("message", "Item added to cart");
        return response;
    }


    @GetMapping("/list")
    public String shoppingList(HttpSession session, Model model, @RequestParam(value = "user_image", required = false, defaultValue = "/static/images/person/p1.jpg") String user_image) {
        // 从 Session 中获取当前用户
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
        return "shopping/list"; // 返回商城页面
    }
    @RequestMapping("/shop")
    public String shop(HttpSession session, Model model, @RequestParam(value = "user_image", required = false, defaultValue = "/static/images/default-avatar.png") String user_image) {
        // 从 Session 中获取当前用户
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

        return "shopping/shop"; // 返回购物页面
    }

    @RequestMapping("/collect")
    public String collect(HttpSession session, Model model) {
        // 从 Session 中获取当前用户
        User u = (User) session.getAttribute("currentUser");
        if (u != null) {
            // 如果用户已登录，显示用户信息
            model.addAttribute("username", u.getUsername());
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

        return "shopping/collect"; // 返回收藏页面
    }

    @GetMapping("/cart")
    public String cart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/user/login";
        }

        List<Cart> cartItems = cartService.getCartItemsByUserId(user.getId().longValue());
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("showAdminButton", user.getGrade() == User.Grade.ADMIN);
        return "shopping/cart";

    }

    @GetMapping("/removeFromCart")
    public String removeFromCart(@RequestParam Long id, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/user/login";
        }

        cartService.deleteCartItem(id);
        return "redirect:/shopping/cart";
    }
    @RequestMapping("/profile")
    public String profile(HttpSession session, Model model) {
        // 从 Session 中获取当前用户
        User u = (User) session.getAttribute("currentUser");
        if (u != null) {
            // 如果用户已登录，显示用户信息
            model.addAttribute("username", u.getUsername());
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

        return "shopping/profile"; // 返回个人信息页面
    }
//    @PostMapping("/update-avatar")
//    public String updateAvatar(@RequestParam("avatar") MultipartFile avatar, HttpSession session) {
//        if (!avatar.isEmpty()) {
//            try {
//                // 获取文件名
//                String fileName = avatar.getOriginalFilename();
//                // 设置文件存储的目录
//                String uploadDir = "src/main/resources/static/images/person/";
//                File uploadDirFile = new File(uploadDir);
//
//                // 如果目录不存在，创建目录
//                if (!uploadDirFile.exists()) {
//                    uploadDirFile.mkdirs();
//                }
//
//                // 设置文件存储路径
//                String filePath = uploadDir + fileName;
//                File dest = new File(filePath);
//
//                // 将文件写入目标路���
//                avatar.transferTo(dest);
//
//                // 获取当前用户对象
//                User user = (User) session.getAttribute("currentUser");
//                if (user == null) {
//                    throw new IllegalStateException("User is not found in session.");
//                }
//
//                // 更新用户头像路径
//                user.setUser_image("/images/person/" + fileName);
//                userService.updateUserImage(user);
//
//                // 更新 session 中的用户对象
//                session.setAttribute("currentUser", user);
//
//                // 返回到个人页面或其他页面
//                return "redirect:/shopping/profile";
//            } catch (IOException e) {
//                e.printStackTrace();
//                // 处理文件上传错误
//                return "redirect:/shopping/profile?error=upload";
//            }
//        }
//        return "redirect:/shopping/profile"; // 如果上传失败，重定向到个人中心页面
//    }
@PostMapping("/update-avatar")
public String updateAvatar(@RequestParam("avatar") MultipartFile avatar, HttpSession session) {
    if (!avatar.isEmpty()) {
        try {
            // Get the file name
            String fileName = avatar.getOriginalFilename();
            // Set the file storage directory
            String uploadDir = new File("src/main/resources/static/images/person/").getAbsolutePath();
            File uploadDirFile = new File(uploadDir);

            // If the directory does not exist, create it
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            // Set the file storage path
            String filePath = uploadDir + File.separator + fileName;
            File dest = new File(filePath);

            // Write the file to the target path
            avatar.transferTo(dest);

            // Get the current user object
            User user = (User) session.getAttribute("currentUser");
            if (user == null) {
                throw new IllegalStateException("User is not found in session.");
            }

            // Update the user avatar path
            user.setUser_image("/images/person/" + fileName);
            userService.updateUserImage(user);

            // Update the user object in the session
            session.setAttribute("currentUser", user);

            // Redirect to the profile page or another page
            return "redirect:/shopping/profile";
        } catch (IOException e) {
            e.printStackTrace();
            // Handle file upload error
            return "redirect:/shopping/profile?error=upload";
        }
    }
    return "redirect:/shopping/profile"; // If upload fails, redirect to the profile page
    }

}
