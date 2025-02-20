package com.service;

import com.entity.Product;
import com.entity.User;
import com.mapper.ShoppingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ShoppingService {
    @Autowired
    private  ShoppingMapper shoppingMapper;

    public  List<Product> getAllProducts() {
        return shoppingMapper.findAll();
    }
}
