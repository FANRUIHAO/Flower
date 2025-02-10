package com.mapper;

import com.entity.Product;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

public interface ProductMapper {

    List<Product> selectProduct(String keyword) ;
    @Delete("delete from product where id=#{id}")
    void deleteProduct(Integer id);
    @Insert("insert into product(num,proname) values(#{num},#{proname})")
    void insertProduct(Product p);
    @Select("select * from product where id=#{id}")
    Product selectProductById(Integer id);

    @Update("update product set num=#{num}, proname=#{proname} where id=#{id}")
    void updateProduct(Product p);
}
