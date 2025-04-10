package com.service;

import com.entity.*;
import com.github.pagehelper.PageInfo;
import com.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CommentMapper commentMapper;
    public List<Order> getOrdersByUserId(Integer id) {
        return orderMapper.findByUserId(id);
    }
    public List<Order> createOrder(User user, List<Cart> selectedItems, String address) {
        List<Order> orders = new ArrayList<>();

        for (Cart item : selectedItems) {
            // 从商品表中查询商品详情
            Product product = productMapper.selectProductByName(item.getCname());
            Order order = new Order();
            order.setUser_id(user.getId());
            order.setPhone(user.getPhone());
            order.setAddr(address);
            order.setUsername(user.getUsername());
            order.setProduct(item.getCname());
            order.setNum(item.getCnum());
            order.setSum(item.getCprice() * item.getCnum());
            order.setStatus("等待发货");
            // 设置订单时间为当前时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String datetime = sdf.format(new Date(System.currentTimeMillis()));
            order.setOrdertime(datetime); // 设置当前时间为字符串格式
            order.setImage(product.getPro_image()); // 假设字段为 image（商品图片路径）
            // 保存订单
            orderMapper.addOrder(order);

            orders.add(order);
        }

        return orders;
    }
    public PageInfo<Order> getAllOrdersWithPagination(int pageNum, int pageSize) {
        // 设置分页参数
        com.github.pagehelper.PageHelper.startPage(pageNum, pageSize);
        // 查询所有订单
        List<Order> orders = orderMapper.findAllOrders();
        // 使用 PageInfo 封装查询结果
        PageInfo<Order> pageInfo = new PageInfo<>(orders);
        return pageInfo;
    }
    public Order findById(Long id) {
        return orderMapper.findById(id.intValue());
    }
    public void save(Order order) {
        orderMapper.saveOrder(order);
    }
    public void confirmReceipt(Long id) {
        Order order = orderMapper.findById(id.intValue());
        if (order != null) {
            order.setStatus("已收货");
            orderMapper.updateOrder(order);
        }
    }
    public void reviewOrder(Long id) {
        Order order = orderMapper.findById(id.intValue());
        if (order != null) {
            order.setStatus("已评价");
            orderMapper.updateOrder(order);
        }
    }
    public void addComment(String username, String product, String comment) {
        Comment newComment = new Comment(null, product, comment, username);

        commentMapper.addComment(newComment);
    }
}
