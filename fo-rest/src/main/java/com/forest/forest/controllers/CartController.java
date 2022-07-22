package com.forest.forest.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.forest.forest.models.Cart;
import com.forest.forest.models.User;
import com.forest.forest.service.CartService;
import com.forest.forest.service.UserService;
import com.forest.forest.utils.JWTUtil;

import io.jsonwebtoken.JwtException;

@CrossOrigin
@RestController
@RequestMapping("/carts")
public class CartController {
    @Autowired 
    private CartService cartService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @GetMapping
    public List<Cart> findAll(HttpServletResponse res, @RequestHeader("Authorization") String authToken) throws IOException {
        String[] token = authToken.split(" ");
        if(!token[0].equals("Bearer")) {
            res.sendRedirect("/auth/nobearer");
        }
        try {
            JWTUtil.verifyUserToken(token[1]);
            return cartService.findAll();
        } catch(JwtException e) {
            res.sendRedirect("/auth/invalid");
            return null;
        }
    }

    @ResponseBody
    @GetMapping("/{user}")
    public List<Cart> findAllByUser(HttpServletResponse res, @RequestHeader("Authorization") String authToken, @PathVariable("user") String username) throws IOException {
        String[] token = authToken.split(" ");
        if(!token[0].equals("Bearer")) {
            res.sendRedirect("/auth/nobearer");
        }
        try {
            String uname = JWTUtil.verifyUserToken(token[1]);
            if(!uname.equals(username)) {
                throw new JwtException("username doesn't match");
            }
            User user = userService.findByUsername(username);
            return cartService.findByUsername(user);
        } catch(JwtException e) {
            res.sendRedirect("/auth/invalid");
            return null;
        }
    }
}