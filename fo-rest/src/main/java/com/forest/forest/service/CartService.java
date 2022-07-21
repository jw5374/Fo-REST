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

    public List<Cart> findCartsByUsername(User user) {
        return cartRepository.findByUser(user);        
    }
}
