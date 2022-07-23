package com.forest.forest.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.forest.forest.models.Cart;
import com.forest.forest.models.Product;
import com.forest.forest.models.User;
import com.forest.forest.service.CartService;
import com.forest.forest.service.ProductService;
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

    @Autowired
    private ProductService productService;

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

    @DeleteMapping("/{user}")
    public boolean deleteCart(HttpServletResponse res, @RequestHeader("Authorization") String authToken, @PathVariable("user") String username, @RequestBody Cart cart) throws IOException {
        String[] token = authToken.split(" ");
        if(!token[0].equals("Bearer")) {
            res.sendRedirect("/auth/nobearer");
        }
        try {
            String uname = JWTUtil.verifyUserToken(token[1]);
            if(!uname.equals(username)) {
                throw new JwtException("username doesn't match");
            }
            return cartService.deleteCart(cart);
        } catch(JwtException e) {
            res.sendRedirect("/auth/invalid");
            return false;
        }
    }

    @DeleteMapping("/{user}/checkout")
    public boolean checkoutCart(HttpServletResponse res, @RequestHeader("Authorization") String authToken, @PathVariable("user") String username, @RequestBody List<Cart> carts) throws IOException {
        String[] token = authToken.split(" ");
        if(!token[0].equals("Bearer")) {
            res.sendRedirect("/auth/nobearer");
        }
        try {
            String uname = JWTUtil.verifyUserToken(token[1]);
            if(!uname.equals(username)) {
                throw new JwtException("username doesn't match");
            }
            carts.forEach((c) -> {
                Product prod = productService.findById(c.getProduct().getId());
                if(prod.getCount() == 0) {
                    cartService.deleteCart(c);
                    return;
                }
                if(c.getCount() > prod.getCount() || prod.getCount() < 0) {
                    prod.setCount(0);
                    cartService.deleteCart(c);
                    return;
                }
                prod.setCount(prod.getCount() - c.getCount());
                cartService.deleteCart(c);
                productService.updateProductStock(prod.getCount(), prod.getId());
            });
            return true;
        } catch(JwtException e) {
            res.sendRedirect("/auth/invalid");
            return false;
        }
    }

    @PutMapping("/{user}")
    public int updateCarts(HttpServletResponse res, @RequestHeader("Authorization") String authToken, @PathVariable("user") String username, @RequestBody List<Cart> carts) throws IOException {
        String[] token = authToken.split(" ");
        if(!token[0].equals("Bearer")) {
            res.sendRedirect("/auth/nobearer");
        }
        try {
            String uname = JWTUtil.verifyUserToken(token[1]);
            if(!uname.equals(username)) {
                throw new JwtException("username doesn't match");
            }
            carts.forEach((c) -> {
                if(c.getCount() == 0) {
                    cartService.deleteCart(c);
                    return;
                }
                cartService.updateCartCount(c.getCount(), c.getCartid());
            });
            return 1;
        } catch(JwtException e) {
            res.sendRedirect("/auth/invalid");
            return -1;
        }
    }
}