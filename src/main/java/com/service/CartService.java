package com.service;

import com.entity.Cart;
import com.entity.Order;
import com.entity.Product;
import com.entity.User;
import com.mapper.CartMapper;
import com.mapper.UserMapper;
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
    public void deleteCartItem(Integer id) {
        cartMapper.deleteCartItem(id);
    }
    public boolean updateCartItemQuantity(Integer id, Integer quantity) {
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

    public void removeMultipleItems(Integer userId, List<Long> itemIds) {
        for (Long itemId : itemIds) {
            cartMapper.deleteCartItem(itemId.intValue());
        }
    }
    public List<Cart> getSelectedCartItems(Integer userId, List<Integer> itemIds) {
        return cartMapper.findSelectedItemsByUserId(userId, itemIds);
    }
}

