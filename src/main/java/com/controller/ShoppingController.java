package com.controller;

import com.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopping")
public class ShoppingController {
    @GetMapping("/")
    public String home() {
        return "redirect:/shopping/list"; // 根路径跳转到购物列表页面
    }

    @GetMapping("/list")
    public String shoppingList(HttpSession session, Model model) {
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
        return "shopping/list"; // 返回商城页面
    }
    @RequestMapping("/shop")
    public String shop(HttpSession session, Model model) {
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

        return "shopping/shop"; // 返回购物页面
    }
    @RequestMapping("/cart")
    public String cart(HttpSession session, Model model) {
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

        return "shopping/cart"; // 返回购物车页面
    }

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam String productName, @RequestParam String productPrice, @RequestParam String productImage, HttpSession session) {
        List<Map<String, String>> cart = (List<Map<String, String>>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        Map<String, String> item = new HashMap<>();
        item.put("name", productName);
        item.put("price", productPrice);
        cart.add(item);
        session.setAttribute("cart", cart);
        return "redirect:/shopping/cart";
    }
    @GetMapping("/cart")
    public String showCart(HttpSession session, Model model) {
        List<Map<String, String>> cart = (List<Map<String, String>>) session.getAttribute("cart");
        model.addAttribute("cart", cart);
        return "shopping/cart";
    }

}
