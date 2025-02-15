package com.controller;

import com.entity.Cart;
import com.entity.User;
import com.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopping")
public class ShoppingController {
    @Autowired
    private CartService cartService;
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

    @PostMapping("/addToCart")
    public String addToCart(@RequestParam String productName, @RequestParam String productPrice, @RequestParam String productImage, @RequestParam Integer quantity, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/user/login";
        }

        Cart cart = new Cart();
        cart.setUser_id(user.getId());
        cart.setCname(productName);
        cart.setCprice(new BigDecimal(productPrice).intValue());
        cart.setImage_url(productImage);
        cart.setCnum(quantity); // 设置数量
        cartService.save(cart);

        return "redirect:/shopping/cart";
    }
//    @PostMapping("/addToCart")
//    public String addToCart(@RequestParam String productName, @RequestParam String productPrice, @RequestParam String productImage, HttpSession session) {
//        List<Map<String, String>> cart = (List<Map<String, String>>) session.getAttribute("cart");
//        if (cart == null) {
//            cart = new ArrayList<>();
//        }
//        Map<String, String> item = new HashMap<>();
//        item.put("name", productName);
//        item.put("price", productPrice);
//        item.put("image", productImage); // Ensure image URL is added
//        cart.add(item);
//        session.setAttribute("cart", cart);
//        return "redirect:/shopping/cart";
//    }

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
//    @RequestMapping("/cart")//实现购物车页面用户登陆状态显示
//    public String cart(HttpSession session, Model model) {
//        // 从 Session 中获取当前用户
//        User u = (User) session.getAttribute("currentUser");
//        if (u != null) {
//            // 如果用户已登录，显示用户信息
//            model.addAttribute("username", u.getUsername());
//            // 根据用户等级判断是否显示后台管理按钮
//            if (u.getGrade() == User.Grade.ADMIN) {
//                model.addAttribute("showAdminButton", true);
//            } else {
//                model.addAttribute("showAdminButton", false);
//            }
//        } else {
//            // 如果未登录，显示游客欢迎信息
//            model.addAttribute("username", "游客");
//            model.addAttribute("showAdminButton", false);
//        }
//
//        return "shopping/cart"; // 返回购物车页面
//    }

}
