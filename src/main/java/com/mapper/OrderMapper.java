package com.mapper;

import com.entity.Order;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface OrderMapper {
    @Select("SELECT * FROM `order`  WHERE user_id = #{id}")
    List<Order> findByUserId(Integer id);
}
