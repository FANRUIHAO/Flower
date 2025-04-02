package com.mapper;

import com.entity.Product;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
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
    @Insert("insert into collect values( #{productName}, #{productPrice}, #{productImage})")
    void addToFavorite(long l, String productName, Double productPrice, String productImage);
    @Select("select count(*) from collect where id = #{l} and product = #{productName}")
    int isProductCollected(Integer l, String productName);
}
