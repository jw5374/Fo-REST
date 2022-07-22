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

    @GetMapping("/carts")
    public List<Cart> findAll(){
        return cartService.findAll();
    }

    @GetMapping("/getCartsForCurrentUser")
    public List<Cart> findAllByUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = new User();
        user.setUsername(username);
        return cartService.findCartsByUsername(user);
    }
}