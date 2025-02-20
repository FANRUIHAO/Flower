package com.service;

import com.entity.Product;
import com.mapper.ProductMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;

    public PageInfo<Product> selectProduct(String keyword,int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> list = productMapper.selectProduct(keyword);
        return new PageInfo<>(list);
    }

    public void deleteProduct(Integer id) {
        productMapper.deleteProduct(id);
    }

    public void saveProduct(Product p) {
        if (p.getId() != null) {
            productMapper.updateProduct(p);
        } else {
            productMapper.insertProduct(p);
        }
    }

    public Product selectProductById(Integer id) {
        return productMapper.selectProductById(id);

    }

    public void updateProduct(Product p) {
        productMapper.updateProduct(p);
    }


}
