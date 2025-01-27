package com.mapper;

import com.entity.Product;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProductMapper {
    @Select("select * from product")
    List<Product> selectall() ;
}
