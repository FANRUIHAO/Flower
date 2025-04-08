package com.service;

import com.entity.Order;
import com.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    public List<Order> getOrdersByUserId(Integer id) {
        return orderMapper.findByUserId(id);
    }
}
