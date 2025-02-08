package com.mapper;

import com.entity.Product;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

public interface ProductMapper {
    @Select("select * from product")
    List<Product> selectall() ;
    @Delete("delete from product where id=#{id}")
    void deleteProduct(Integer id);

}
