package com.mapper;

import com.entity.Product;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ProductMapper {
    @Select("")
    public List<Product> selectall() ;
}
