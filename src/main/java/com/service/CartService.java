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
    private CartMapper cartmapper;

    public void addToCart(HttpSession session, Integer id) {
        List<Cart> cart = (List<Cart>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        // 检查是否已存在
        for (Cart item : cart) {
            if (item.getId().equals(id)) {
                item.setCnum(item.getCnum() + 1);
                session.setAttribute("cart", cart);
                return;
            }
        }

        // 获取商品信息
        Product product = cartmapper.getProductById(id);
        Cart newItem = new Cart();
        newItem.setId(product.getId());
        newItem.setCname(product.getProname());
        newItem.setCprice(product.getPrice());
        newItem.setCnum(1);
        cart.add(newItem);

        session.setAttribute("cart", cart);
    }

    public List<Cart> getCart(HttpSession session) {
        List<Cart> cart = (List<Cart>) session.getAttribute("cart");
        return cart == null ? new ArrayList<>() : cart;
    }

    public int getCartCount(HttpSession session) {
        List<Cart> cart = getCart(session);
        return cart.stream().mapToInt(Cart::getCnum).sum();
    }
}

