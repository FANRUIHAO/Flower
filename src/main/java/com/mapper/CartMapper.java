package com.mapper;

import com.entity.Cart;
import com.entity.Product;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartMapper {

    @Insert("insert into cart (cname, cprice, cnum, user_id, image_url) values ( #{cname}, #{cprice}, #{cnum}, #{user_id}, #{image_url})")
    void save(Cart cart);
    @Select("select * from cart where user_id=#{userId}")
    List<Cart> findByUserId(Integer userId);
    @Delete("delete from cart where id=#{id}")
    void deleteCartItem(Long id);
    @Select("select * from cart where id=#{id}")
    Cart findById(Long id);
    @Update("update cart set cnum=#{cnum} where id=#{id}")
    void updateCartItem(Cart cartItem);
}
