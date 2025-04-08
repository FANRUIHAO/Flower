package com.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.entity.Order;
import com.entity.User;
import com.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    public String getOrders(HttpSession session, Model model) {
        User u = (User) session.getAttribute("currentUser");
        if (u != null) {
            // 已登录用户
            model.addAttribute("username", u.getUsername());
            model.addAttribute("showAdminButton", u.getGrade() == User.Grade.ADMIN);

            // 获取用户订单
            List<Order> orders = orderService.getOrdersByUserId(u.getId());
            System.out.println("订单列表: " + orders);
            model.addAttribute("orders", orders);
        } else {
            // 未登录用户
            model.addAttribute("username", "游客");
            model.addAttribute("showAdminButton", false);
        }

        return "shopping/order"; // 返回order.html页面
    }
}