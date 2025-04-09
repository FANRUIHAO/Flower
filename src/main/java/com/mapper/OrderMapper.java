package com.mapper;

import com.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrderMapper {
    @Select("SELECT * FROM `order`  WHERE user_id = #{id}")
    List<Order> findByUserId(Integer id);
    @Insert("INSERT INTO `order` (user_id, product, addr, num, sum, status, ordertime, image, phone) " +
            "VALUES (#{user_id}, #{product}, #{addr}, #{num}, #{sum}, #{status}, #{ordertime}, #{image}, #{phone})")
    void addOrder(Order order);
}
