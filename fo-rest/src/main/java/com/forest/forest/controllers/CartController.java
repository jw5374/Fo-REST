package com.forest.forest.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	private static Logger log = LogManager.getLogger(CartController.class);

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
        log.info("Attempting to fetch all Carts");
        if(!token[0].equals("Bearer")) {
            log.warn("Fetch all Carts: Found no bearer");
            res.sendRedirect("/auth/nobearer");
        }
        else{
            try {
                log.info("Fetch all Carts: Verifying user token");
                JWTUtil.verifyUserToken(token[1]);
                log.info("Fetch all Carts: Successfully Verified user token, Returning Cart list as uname");
                return cartService.findAll();
            } catch(JwtException e) {
                log.warn("Fetch all Carts: Caught JwtException");
                res.sendRedirect("/auth/invalid");
                
            }
        }
        log.warn("Fetch all Carts: Failure, returning null");
        return null;
    }

    @ResponseBody
    @GetMapping("/{user}")
    public List<Cart> findAllByUser(HttpServletResponse res, @RequestHeader("Authorization") String authToken, @PathVariable("user") String username) throws IOException {
        String[] token = authToken.split(" ");
        log.info("Attempting to fetch all Carts by User ("+username+")");
        if(!token[0].equals("Bearer")) {
            log.warn("Fetch all Carts by user ("+username+"): Found no bearer");
            res.sendRedirect("/auth/nobearer");
        }
        else{
            try {
                log.info("Fetch all Carts by User ("+username+"): Verifying user token");
                String uname = JWTUtil.verifyUserToken(token[1]);
                log.info("Fetch all Carts by user ("+username+"): Successfully verified user token as uname");
                if(!uname.equals(username)) {
                    log.warn("Fetch all Carts by User ("+username+"): Given username != uname");
                    throw new JwtException("username doesn't match");
                }
                log.info("Fetch all carts by User ("+username+"): Finding user by Username");
                User user = userService.findByUsername(username);
                log.info("Fetch all Carts by User ("+username+"): Returning Cart List by User");
                return cartService.findByUsername(user);
            } catch(JwtException e) {
                log.warn("Fetch all Carts by User ("+username+"): Caught JwtException");
                res.sendRedirect("/auth/invalid");
                
            }
        }
        log.info("Fetch all Carts by User ("+username+"): Failure, Returning null");
        return null;
    }

    // find all by user AND product id?
    @ResponseBody
    @GetMapping("/{user}/{productid}")
    public Cart findAllByUser(HttpServletResponse res, @RequestHeader("Authorization") String authToken, @PathVariable("user") String username, @PathVariable("productid") long productId) throws IOException {
        String[] token = authToken.split(" ");
        log.info("Attempting to fetch a Cart by User ("+username+") and Product ("+productId+")");
        if(!token[0].equals("Bearer")) {
            log.warn("Fetch a Cart by User ("+username+") and Product (" + productId +"): Found no bearer");
            res.sendRedirect("/auth/nobearer");
        }
        else{
            try {
                log.info("Fetch a Cart by User ("+username+") and Product ("+productId+"): Verifying user token");
                String uname = JWTUtil.verifyUserToken(token[1]);
                log.info("Fetch a Cart by User ("+username+") and Product ("+productId+"): Successfully verified user token as uname");
                if(!uname.equals(username)) {
                    log.warn("Fetch a Cart by User ("+username+") and Product ("+productId+"): Given username != uname");
                    throw new JwtException("username doesn't match");
                }
                log.info("Fetch a Cart by User ("+username+") and Product ("+productId+"): Returning Cart by username and productId");
                return cartService.findByUsernameAndProduct(username, productId);
            } catch(JwtException e) {
                log.warn("Fetch a Cart by User ("+username+") and Product ("+productId+"): Caught JwtException");
                res.sendRedirect("/auth/invalid");
            }
        }
        log.info("Fetch a Cart by User ("+username+") and Product ("+productId+"): Failure, Returning null");
        return null;
    }

    @DeleteMapping("/{user}")
    public boolean deleteCart(HttpServletResponse res, @RequestHeader("Authorization") String authToken, @PathVariable("user") String username, @RequestBody Cart cart) throws IOException {
        String[] token = authToken.split(" ");
        log.info("Attempting to delete a Cart by User ("+username+") and cart ("+cart.getCartid()+")");
        if(!token[0].equals("Bearer")) {
            log.warn("Delete a Cart by User ("+username+") and cart ("+cart.getCartid()+"): Found no bearer");
            res.sendRedirect("/auth/nobearer");
        }
        else{
            try {
                log.info("Delete a Cart by User ("+username+") and cart ("+cart.getCartid()+"): Verifying user token");
                String uname = JWTUtil.verifyUserToken(token[1]);
                log.info("Delete a Cart by User ("+username+") and cart ("+cart.getCartid()+"): Successfully verified user token as uname");
                if(!uname.equals(username)) {
                    log.warn("Delete a Cart by User ("+username+") and cart ("+cart.getCartid()+"): Given uname != username");
                    throw new JwtException("username doesn't match");
                }
                log.info("Delete a Cart by User ("+username+") and cart ("+cart.getCartid()+"): Returning boolean from deleting Cart");
                return cartService.deleteCart(cart);
            } catch(JwtException e) {
                log.warn("Delete a Cart by User ("+username+") and cart ("+cart.getCartid()+"): Caught JwtException");
                res.sendRedirect("/auth/invalid");
            }
        }
        log.info("Update a Cart by User ("+username+") and Cart ("+cart.getCartid()+"): Failure, Returning false");
        return false;
    }

    @DeleteMapping("/{user}/checkout")
    public boolean checkoutCart(HttpServletResponse res, @RequestHeader("Authorization") String authToken, @PathVariable("user") String username, @RequestBody List<Cart> carts) throws IOException {
        log.info("Attempting to checkout a Cart by User ("+username+") and Cart List:");
    	String[] token = authToken.split(" ");
        if(!token[0].equals("Bearer")) {
            log.warn("Checkout a Cart by User ("+username+") and Cart List: Found no bearer");
            res.sendRedirect("/auth/nobearer");
        }
        else{
            try {
                log.info("Checkout a Cart by User ("+username+") and Cart List: Verifying user token");
                String uname = JWTUtil.verifyUserToken(token[1]);
                log.info("Checkout a Cart by User ("+username+") and Cart List: Successfully verified user token as uname");
                if(!uname.equals(username)) {
                    log.warn("Checkout a Cart by User ("+username+") and Cart List:  Given uname != username");
                    throw new JwtException("username doesn't match");
                }             
                carts.forEach((c) -> {
                	log.info("Checkout a Cart by User ("+username+") and Cart List: Finding product by Id");
                    Product prod = productService.findById(c.getProduct().getId());
                    if(prod.getCount() == 0) {
                    	log.info("Checkout a Cart by User ("+username+") and Cart List: Product ("+prod.getCount()+") count = zero, removing Cart Entry");
                        cartService.deleteCart(c);
                        return;
                    }
                    if(c.getCount() > prod.getCount() || prod.getCount() < 0) {
                    	log.info("Checkout a Cart by User ("+username+") and Cart List: Product ("+prod.getCount()+") count isn't legal for the Cart Obect purchase amount, Updating Product("+prod.getCount()+") Object Count to 0");
                    	prod.setCount(0);
                    	log.info("Checkout a Cart by User ("+username+") and Cart List: Deleting Cart Entry ("+c.getCartid()+")");
                        cartService.deleteCart(c);
                        return;
                    }
                	//log.info("Checkout a Cart by User ("+username+") and Cart List: Preparing Product ("+prod.getId()+") object Count to reflect purchase");
                    prod.setCount(prod.getCount() - c.getCount());
                	log.info("Checkout a Cart by User ("+username+") and Cart List: Deleting Cart Entry ("+c.getCartid()+")");
                    cartService.deleteCart(c);
                	log.info("Checkout a Cart by User ("+username+") and Cart List: Updating Product ("+prod.getId()+") to reflect purchase");
                    productService.updateProductStock(prod.getCount(), prod.getId());
                });
            	log.info("Checkout a Cart by User ("+username+") and Cart List: Success, Returning true");
                return true;
            } catch(JwtException e) {
            	log.warn("Checkout a Cart by User ("+username+") and Cart List: Caught JwtException");
                res.sendRedirect("/auth/invalid");
            }
        }
        log.info("Checkout a Cart by User ("+username+") and Cart List: Failure, Returning false");
        return false;
    }

    @PutMapping("/{user}")
    public int updateCarts(HttpServletResponse res, @RequestHeader("Authorization") String authToken, @PathVariable("user") String username, @RequestBody List<Cart> carts) throws IOException {
        log.info("Attempting to Update a set of Carts by User ("+username+") and Cart List");
    	String[] token = authToken.split(" ");
        if(!token[0].equals("Bearer")) {
            log.warn("Update a set of Carts by User ("+username+") and Cart List: Found no Bearer");
            res.sendRedirect("/auth/nobearer");
        }
        else{
            try {
                log.info("Update a set of Carts by User ("+username+") and Cart List: Verifying user token");
                String uname = JWTUtil.verifyUserToken(token[1]);
                log.info("Update a set of Carts by User ("+username+") and Cart List: Successfully verified user token as uname");
                if(!uname.equals(username)) {
                    log.warn("Update a set of Carts by User ("+username+") and Cart List: uname != username");
                    throw new JwtException("username doesn't match");
                }
                carts.forEach((c) -> {
                    if(c.getCount() == 0) {
                        log.info("Update a set of Carts by User ("+username+") and Cart List: Deleting cart entry with count 0");
                        cartService.deleteCart(c);
                        return;
                    }
                    log.info("Update a set of Carts by User ("+username+") and Cart List: Updating Cart Entry");
                    cartService.updateCartCount(c.getCount(), c.getCartid());
                });
                log.info("Update a set of Carts by User ("+username+") and Cart List: Success, Returning 1");
                return 1;
            } catch(JwtException e) {
                log.warn("Update a set of Carts by User ("+username+") and Cart List: Caught JwtException");
                res.sendRedirect("/auth/invalid");
            }
        }
        log.info("Update a set of Carts by User ("+username+") and Cart List: Failure, Returning -1");
        return -1;
    }

    @PutMapping("/{user}/item")
    public int updateCart(HttpServletResponse res, @RequestHeader("Authorization") String authToken, @PathVariable("user") String username, @RequestBody Cart cart) throws IOException {
        log.info("Attempting to Update a Cart by User ("+username+") and Cart ("+cart.getCartid()+")");
    	String[] token = authToken.split(" ");
        if(!token[0].equals("Bearer")) {
            log.warn("Update a Cart by User ("+username+") and Cart ("+cart.getCartid()+"): Found no Bearer");
            res.sendRedirect("/auth/nobearer");
        }
        else{
            try {
                log.info("Update a Cart by User ("+username+") and Cart ("+cart.getCartid()+"): Verifying user token");
                String uname = JWTUtil.verifyUserToken(token[1]);
                log.info("Update a Cart by User ("+username+") and Cart ("+cart.getCartid()+"): Successfully verified user token as uname");
                if(!uname.equals(username)) {
                    log.warn("Update a Cart by User ("+username+") and Cart ("+cart.getCartid()+"): uname != username");
                    throw new JwtException("username doesn't match");
                }
                log.info("Update a Cart by User ("+username+") and Cart ("+cart.getCartid()+"): Updating Cart Entry Count and Id fields");
                cartService.updateCartCount(cart.getCount(), cart.getCartid());
                log.info("Update a Cart by User ("+username+") and Cart ("+cart.getCartid()+"): Success, Returning 1");
                return 1;
            } catch(JwtException e) {
                log.warn("Update a Cart by User ("+username+") and Cart ("+cart.getCartid()+"): Caught JwtException");
                res.sendRedirect("/auth/invalid");
            }
        }
        log.info("Update a Cart by User ("+username+") and Cart ("+cart.getCartid()+"): Failure, Returning -1");
        return -1;
    }

    @PostMapping("/{user}")
    public Cart saveCartItem(HttpServletResponse res, @RequestHeader("Authorization") String authToken, @PathVariable("user") String username, @RequestBody Cart cart) throws IOException {
        log.info("Attempting to Save an item to Cart by User ("+username+") and Cart ("+cart.getCartid()+")");
    	String[] token = authToken.split(" ");
    	if(!token[0].equals("Bearer")) {
            log.warn("Save an item to Cart by User ("+username+") and Cart ("+cart.getCartid()+"): Found no Bearer");
            res.sendRedirect("/auth/nobearer");
        }
        else{
            try {
                log.info("Save an item to Cart by User ("+username+") and Cart ("+cart.getCartid()+"): Verifying user token");
                String uname = JWTUtil.verifyUserToken(token[1]);
                log.info("Save an item to Cart by User ("+username+") and Cart ("+cart.getCartid()+"): Successfully verified user token as uname");
                if(!uname.equals(username)) {
                    log.info("Save an item to Cart by User ("+username+") and Cart ("+cart.getCartid()+"): uname != username");
                    throw new JwtException("username doesn't match");
                }
                log.info("Save an item to Cart by User ("+username+") and Cart ("+cart.getCartid()+"): Return and Insert the Cart Entry");
                return cartService.newCartItem(cart);
            } catch(JwtException e) {
                log.info("Save an item to Cart by User ("+username+") and Cart ("+cart.getCartid()+"): Caught JwtException");
                res.sendRedirect("/auth/invalid");
            }
        }
        log.info("Save an item to Cart by User ("+username+") and Cart ("+cart.getCartid()+"): Failure, Return null");
        return null;
    }
}