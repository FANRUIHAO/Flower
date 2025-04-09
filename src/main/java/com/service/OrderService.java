package com.service;

import com.entity.Cart;
import com.entity.Order;
import com.entity.Product;
import com.entity.User;
import com.mapper.CartMapper;
import com.mapper.OrderMapper;
import com.mapper.ProductMapper;
import com.mapper.UserMapper;
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
            order.setProduct(item.getCname());
            order.setNum(item.getCnum());
            order.setSum(item.getCprice() * item.getCnum());
            order.setStatus("等待发货");
            // 设置订单时间为当前时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String datetime = sdf.format(new Date(System.currentTimeMillis()));
            order.setOrdertime(datetime); // 设置当前时间为字符串格式

            order.setImage(product.getPro_image()); // 假设字段为 image（商品图片路径）
//            System.out.println("订单详情: " + order); // 调试输出
//            System.out.println("商品详情: " + product); // 调试输出
            // 保存订单
            orderMapper.addOrder(order);

            orders.add(order);
        }

        return orders;
    }



}
