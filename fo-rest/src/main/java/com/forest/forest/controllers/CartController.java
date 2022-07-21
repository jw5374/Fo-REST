package com.forest.forest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.forest.forest.models.Cart;
import com.forest.forest.models.User;
import com.forest.forest.service.CartService;

@CrossOrigin
@RestController
public class CartController {
    @Autowired 
    private CartService cartService;

    @GetMapping("/getCarts")
    public List<Cart> findAll() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = new User();
        user.setUsername(username);
        return cartService.findCartsByUsername(user);
    }
}
