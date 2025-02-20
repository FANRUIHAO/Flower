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
}
