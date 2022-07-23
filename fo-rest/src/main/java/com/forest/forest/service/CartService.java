package com.forest.forest.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forest.forest.models.Cart;
import com.forest.forest.models.User;
import com.forest.forest.repository.CartRepository;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;

    public List<Cart> findAll() {
        return cartRepository.findAll();        
    }

    public List<Cart> findByUsername(User user) {
        return cartRepository.findByUser(user);        
    }

    public boolean deleteCart(Cart cart) {
        cartRepository.delete(cart);
        return true;
    }

    public int updateCartCount(int count, int id) {
        return cartRepository.updateCartCount(count, id);
    }
}
