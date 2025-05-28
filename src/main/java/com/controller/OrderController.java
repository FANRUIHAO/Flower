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
            model.addAttribute("userImage", u.getUser_image());
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
    @PostMapping("/addComment")
    @ResponseBody
    public Map<String, Object> addComment(@RequestBody Map<String, Object> payload, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {

            User currentUser = (User) session.getAttribute("currentUser");
            if (currentUser == null) {
                throw new IllegalStateException("用户未登录");
            }

            Long orderId = Long.valueOf(payload.get("orderId").toString());
            String comment = payload.get("comment").toString();

            Order order = orderService.findById(orderId);
            if (order == null) {
                throw new IllegalStateException("订单不存在");
            }

            orderService.addComment(currentUser.getUsername(), order.getProduct(), comment);
            orderService.reviewOrder(orderId);
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

}