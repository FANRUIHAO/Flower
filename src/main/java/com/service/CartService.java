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
    public List<Cart> getCartItemsByUserId(Integer userId) {
        return cartMapper.findByUserId(userId);
    }

    public void deleteCartItem(Long id) {
        cartMapper.deleteCartItem(id);
    }


    public boolean updateCartItemQuantity(Long id, Integer quantity) {
        Cart cartItem = cartMapper.findById(id);
        if (cartItem != null) {
            cartItem.setCnum(quantity);
            cartMapper.updateCartItem(cartItem);
            return true;
        }
        return false;
    }

    public Cart findByUserIdAndProductName(Integer id, String productName) {
        List<Cart> cartItems = cartMapper.findByUserId(id);
        for (Cart cartItem : cartItems) {
            if (cartItem.getCname().equals(productName)) {
                return cartItem;
            }
        }
        return null;
    }

    public void update(Cart existingItem) {
        cartMapper.updateCartItem(existingItem);
    }

    public int getCartItemCount(Integer id) {
        List<Cart> cartItems = cartMapper.findByUserId(id);
        int count = 0;
        for (Cart cartItem : cartItems) {
            count += cartItem.getCnum();
        }
        return count;
    }
}

