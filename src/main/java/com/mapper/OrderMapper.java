package com.mapper;

import com.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface OrderMapper {
    @Select("SELECT * FROM `order`  WHERE user_id = #{id}")
    List<Order> findByUserId(Integer id);
    @Insert("INSERT INTO `order` (user_id, product, addr, num, sum, status, ordertime, image, phone, username) " +
            "VALUES (#{user_id}, #{product}, #{addr}, #{num}, #{sum}, #{status}, #{ordertime}, #{image}, #{phone}, #{username})")
    void addOrder(Order order);
    @Select("SELECT * FROM `order`")
    List<Order> findAllOrders();
    @Select("SELECT * FROM `order` WHERE id = #{orderId}")
    Order findById(Integer orderId);
    @Update("UPDATE `order` SET status = #{status} WHERE id = #{id}")
    void saveOrder(Order order);
    @Update("UPDATE `order` SET product = #{product}, addr = #{addr}, num = #{num}, sum = #{sum}, status = #{status}, ordertime = #{ordertime}, image = #{image}, phone = #{phone}, username = #{username} WHERE id = #{id}")
    void updateOrder(Order order);
}
