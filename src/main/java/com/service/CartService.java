package com.service;

import com.entity.Cart;
import com.entity.Product;
import com.mapper.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartMapper cartMapper;
    public void save(Cart cart) {
        cartMapper.save(cart);
    }
    public List<Cart> getCartItemsByUserId(Long userId) {
        return cartMapper.findByUserId(userId);
    }

    public void deleteCartItem(Long id) {
        cartMapper.deleteCartItem(id);
    }
}

