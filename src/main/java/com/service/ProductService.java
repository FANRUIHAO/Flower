package com.service;

import com.entity.Product;
import com.mapper.ProductMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;

    public List<Product> selectProduct() {
        List<Product> list=productMapper.selectall();
        return list;
    }
}
