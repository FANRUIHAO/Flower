package com.service;

import com.entity.Order;
import com.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    public List<Order> getOrdersByUserId(Integer id) {
        return orderMapper.findByUserId(id);
    }

    public List<Order> createOrder(Integer userId, List<Integer> itemIds, String address, double totalAmount) {
        List<Order> orders = new ArrayList<>();

        for (Integer itemId : itemIds) {
            // Create a new Order object for each item
            Order order = new Order();
            order.setUser_id(userId);
            order.setAddr(address);
            order.setProduct("Product Name for ID: " + itemId); // Replace with actual product name retrieval logic
            order.setNum(1); // Replace with actual quantity if needed
            order.setSum(totalAmount / itemIds.size()); // Divide total amount equally or replace with actual price
            order.setStatus("Pending");
            order.setOrdertime(String.valueOf(System.currentTimeMillis()));
            order.setImage()
            order.setPhone(); // Replace with actual phone number retrieval logic
            // Save the order using the mapper
            orderMapper.addOrder(order);

            // Add the created order to the list
            orders.add(order);
        }

        return orders;
    }
}
