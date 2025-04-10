package com.controller;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import com.entity.Order;
import com.entity.User;
import com.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            model.addAttribute("orders", orders);
        } else {
            // 未登录用户
            model.addAttribute("username", "游客");
            model.addAttribute("showAdminButton", false);
        }
        return "shopping/order";
    }
    @GetMapping("/management")
    public String listOrders(@RequestParam(defaultValue = "1") int pageNum,
                             @RequestParam(defaultValue = "10") int pageSize,
                             Model model) {
        // 调用服务层方法获取分页数据
        PageInfo<Order> pageInfo = orderService.getAllOrdersWithPagination(pageNum, pageSize);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("orders", pageInfo.getList()); // 获取当前页的订单列表
        return "order/list"; // 返回模板名称
    }
    @PostMapping("/updateStatus")
    @ResponseBody
    public Map<String, Object> updateStatus(@RequestParam Long id, @RequestParam String status) {
        Map<String, Object> result = new HashMap<>();
        System.out.println("111");
        try {
            // 根据id查找订单并更新状态
            Order order = orderService.findById(id);
            order.setStatus(status);
            orderService.save(order);

            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
    @PostMapping("/confirmReceipt")
    @ResponseBody
    public Map<String, Object> confirmReceipt(@RequestParam Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            orderService.confirmReceipt(id);
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    @PostMapping("/review")
    @ResponseBody
    public Map<String, Object> review(@RequestParam Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            orderService.reviewOrder(id);
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }
}