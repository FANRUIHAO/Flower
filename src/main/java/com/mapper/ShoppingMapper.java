package com.mapper;

import com.entity.Collect;
import com.entity.Product;
import org.apache.ibatis.annotations.*;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;

import java.util.List;
import java.util.Map;

public interface ShoppingMapper{

    @Select("select * from product")
    List<Product> findAll();
    @Select("select * from product where category = #{category} and price < #{price}")
    List<Product> filterProducts(String category, double price);
    @Select("select * from product where proname = #{flowerName}")
    Product getProductsByFlowerName(String flowerName);
    @Select("select * from comment where cproduct = #{cproduct}")
    void showComment(String comment, String cproduct);

    @Insert("insert into collect (username, product, price, image) values (#{username}, #{productName}, #{productPrice}, #{productImage})")
    void addToFavorite(@Param("username") String username, @Param("productName") String productName, @Param("productPrice") Integer productPrice, @Param("productImage") String productImage);
    @Select("SELECT COUNT(*) FROM collect WHERE username = #{username} AND product = #{productName}")
    int isProductCollected(@Param("username") String username, @Param("productName") String productName);
    @Delete("delete from collect where username = #{username} and product = #{productName}")
    void removeFromFavorite(@Param("username")String username, @Param("productName") String productName);
    @Select("select * from collect where username = #{username}")
    List<Collect> listcollect(String username);
}
