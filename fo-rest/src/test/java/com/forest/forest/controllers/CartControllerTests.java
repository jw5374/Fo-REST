package com.forest.forest.controllers;

import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;

import com.forest.forest.models.Cart;
import com.forest.forest.models.Product;
import com.forest.forest.models.User;
import com.forest.forest.service.CartService;
import com.forest.forest.service.ProductService;
import com.forest.forest.service.UserService;
import com.forest.forest.utils.JWTUtil;

import io.jsonwebtoken.JwtException;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CartControllerTests {

    @InjectMocks
    private CartController cartController;

    @MockBean
    private CartService cartService;

    @MockBean
    private UserService userService;

    @MockBean
    private ProductService productService;

    private MockHttpServletResponse mockHttpServletResponse;
    private User user;
    private Product product;
    private Cart cart;

    private String noBearerUrl = "/auth/nobearer";
    private String invalidAuthUrl = "/auth/invalid";
    private String validToken;

    @BeforeEach
    public void init(){
        mockHttpServletResponse = new MockHttpServletResponse();
        user = new User("username", "password", "user@gmail.com", "123 Test Avenue");
        product = new Product(1, "Test Product", "Test Description", 4, 2);
        cart = new Cart(1, new Timestamp(1), user, product, 3);
        validToken = "Bearer " + JWTUtil.generateUserToken(user);
    }

    @AfterEach
    public void teardown(){
        mockHttpServletResponse = null;
        user = null;
        product = null;
        cart = null;
        validToken = null;
    }

    @Test
    public void testFindAllVerifiesUserToken(){
        List<Cart> carts = new ArrayList<Cart>();

        carts.add(cart);
        carts.add(cart);
        carts.add(cart);

        when(cartService.findAll()).thenReturn(carts);

        try{
            assertThat(cartController.findAll(mockHttpServletResponse, validToken)).isEqualTo(carts);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testFindAllByUserVerifiesUserToken(){
        List<Cart> carts = new ArrayList<Cart>();

        carts.add(cart);
        carts.add(cart);
        carts.add(cart);

        when(userService.findByUsername(user.getUsername())).thenReturn(user);
        when(cartService.findByUsername(user)).thenReturn(carts);

        try{
            assertThat(cartController.findAllByUser(mockHttpServletResponse, validToken, user.getUsername())).isEqualTo(carts);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testFindAllByUserAndProductVerifiesToken(){
        Cart c = null;
        when(cartService.findByUsernameAndProduct(user.getUsername(), product.getId())).thenReturn(cart);

        try{
            c = cartController.findAllByUser(mockHttpServletResponse, validToken, user.getUsername(), product.getId());
        } catch (IOException e){
            e.printStackTrace();
        }

        assertThat(c).isEqualTo(cart);
    }

    @Test
    public void testDeleteCartVerifiesToken(){
        boolean bool = false;
        when(cartService.deleteCart(cart)).thenReturn(true);

        try{
            bool = cartController.deleteCart(mockHttpServletResponse, validToken, user.getUsername(), cart);
        } catch (IOException e){
            e.printStackTrace();
        }

        assertTrue(bool);
    }

    @Test
    public void testCheckoutCartVerifiesToken(){
        boolean bool = false;
        List<Cart> carts = new ArrayList<Cart>();

        carts.add(cart);
        carts.add(cart);
        carts.add(cart);

        when(cartService.deleteCart(cart)).thenReturn(true);
        when(productService.findById(cart.getCartid())).thenReturn(product);
        when(productService.updateProductStock(product.getCount(), product.getId())).thenReturn(1);

        try{
            bool = cartController.checkoutCart(mockHttpServletResponse, validToken, user.getUsername(), carts);
        } catch (IOException e){
            e.printStackTrace();
        }

        assertTrue(bool);
    }

    @Test
    public void testUpdateCartsVerifiesToken(){
        int n = 0;
        List<Cart> carts = new ArrayList<Cart>();

        carts.add(cart);
        carts.add(cart);
        carts.add(cart);

        when(cartService.deleteCart(cart)).thenReturn(true);
        when(cartService.updateCartCount(cart.getCount(), cart.getCartid())).thenReturn(1);

        try{
            n = cartController.updateCarts(mockHttpServletResponse, validToken, user.getUsername(), carts);
        } catch (IOException e){
            e.printStackTrace();
        }

        assertThat(n).isEqualTo(1);
    }

    @Test
    public void testUpdateCartVerifiesToken(){
        int n = 0;

        when(cartService.updateCartCount(cart.getCount(), cart.getCartid())).thenReturn(1);

        try{
            n = cartController.updateCart(mockHttpServletResponse, validToken, user.getUsername(), cart);
        } catch (IOException e){
            e.printStackTrace();
        }

        assertThat(n).isEqualTo(1);
    }

    @Test
    public void testSaveCartItemVerifiesToken(){
        Cart c = null;
        when(cartService.newCartItem(cart)).thenReturn(cart);

        try{
            c = cartController.saveCartItem(mockHttpServletResponse, validToken, user.getUsername(), cart);
        } catch (IOException e){
            e.printStackTrace();
        }

        assertThat(c).isEqualTo(cart);
    }

    @Test
    public void testFindAllWithoutBearerRedirects(){
        try{
            cartController.findAll(mockHttpServletResponse, "test string");
        } catch (IOException e){
            e.printStackTrace();
        }

        assertThat(mockHttpServletResponse.getRedirectedUrl()).isEqualTo(noBearerUrl);
    }

    @Test
    public void testFindAllInvalidTokenRedirects(){
        String badToken = "Bearer invalid";

        try{
            cartController.findAll(mockHttpServletResponse, badToken);
        } catch (IOException e){
            e.printStackTrace();
        } catch (JwtException e){
            assertThat(mockHttpServletResponse.getRedirectedUrl()).isEqualTo(invalidAuthUrl);
        }
    }

    @Test
    public void testFindAllByUserRedirects(){
        try{
            cartController.findAllByUser(mockHttpServletResponse, "test string", user.getUsername());
        } catch (IOException e){
            e.printStackTrace();
        }

        assertThat(mockHttpServletResponse.getRedirectedUrl()).isEqualTo(noBearerUrl);
    }

    @Test
    public void testFindAllByUserInvalidTokenRedirects(){
        String badToken = "Bearer invalid";

        try{
            cartController.findAllByUser(mockHttpServletResponse, badToken, user.getUsername());
        } catch (IOException e){
            e.printStackTrace();
        } catch (JwtException e){
            assertThat(mockHttpServletResponse.getRedirectedUrl()).isEqualTo(invalidAuthUrl);
        }
    }

    @Test
    public void testFindAllByUserAndProductRedirects(){
        try{
            cartController.findAllByUser(mockHttpServletResponse, "test string", user.getUsername(), product.getId());
        } catch (IOException e){
            e.printStackTrace();
        }

        assertThat(mockHttpServletResponse.getRedirectedUrl()).isEqualTo(noBearerUrl);
    }

    @Test
    public void testFindAllByUserAndProductInvalidTokenRedirects(){
        String badToken = "Bearer invalid";

        try{
            cartController.findAllByUser(mockHttpServletResponse, badToken, user.getUsername(), product.getId());
        } catch (IOException e){
            e.printStackTrace();
        } catch (JwtException e){
            assertThat(mockHttpServletResponse.getRedirectedUrl()).isEqualTo(invalidAuthUrl);
        }
    }

    @Test
    public void testDeleteCartRedirects(){
        try{
            cartController.deleteCart(mockHttpServletResponse, "test string", user.getUsername(), cart);
        } catch (IOException e){
            e.printStackTrace();
        }

        assertThat(mockHttpServletResponse.getRedirectedUrl()).isEqualTo(noBearerUrl);
    }

    @Test
    public void testDeleteCartInvalidTokenRedirects(){
        String badToken = "Bearer invalid";

        try{
            cartController.deleteCart(mockHttpServletResponse, badToken, user.getUsername(), cart);
        } catch (IOException e){
            e.printStackTrace();
        } catch (JwtException e){
            assertThat(mockHttpServletResponse.getRedirectedUrl()).isEqualTo(invalidAuthUrl);
        }
    }

    @Test
    public void testCheckoutCartRedirects(){
        List<Cart> carts = new ArrayList<>();

        carts.add(cart);

        try{
            cartController.checkoutCart(mockHttpServletResponse, "test string", user.getUsername(), carts);
        } catch (IOException e){
            e.printStackTrace();
        }

        assertThat(mockHttpServletResponse.getRedirectedUrl()).isEqualTo(noBearerUrl);
    }

    @Test
    public void testCheckoutCartInvalidTokenRedirects(){
        String badToken = "Bearer invalid";
        List<Cart> carts = new ArrayList<>();

        carts.add(cart);

        try{
            cartController.checkoutCart(mockHttpServletResponse, badToken, user.getUsername(), carts);
        } catch (IOException e){
            e.printStackTrace();
        } catch (JwtException e){
            assertThat(mockHttpServletResponse.getRedirectedUrl()).isEqualTo(invalidAuthUrl);
        }
    }

    @Test
    public void testUpdateCartsRedirects(){
        List<Cart> carts = new ArrayList<>();

        carts.add(cart);

        try{
            cartController.updateCarts(mockHttpServletResponse, "test string", user.getUsername(), carts);
        } catch (IOException e){
            e.printStackTrace();
        }

        assertThat(mockHttpServletResponse.getRedirectedUrl()).isEqualTo(noBearerUrl);
    }

    @Test
    public void testUpdateCartsInvalidTokenRedirects(){
        String badToken = "Bearer invalid";
        List<Cart> carts = new ArrayList<>();

        carts.add(cart);
        try{
            cartController.updateCarts(mockHttpServletResponse, badToken, user.getUsername(), carts);
        } catch (IOException e){
            e.printStackTrace();
        } catch (JwtException e){
            assertThat(mockHttpServletResponse.getRedirectedUrl()).isEqualTo(invalidAuthUrl);
        }
    }

    @Test
    public void testUpdateCartRedirects(){
        try{
            cartController.updateCart(mockHttpServletResponse, "test string", user.getUsername(), cart);
        } catch (IOException e){
            e.printStackTrace();
        }

        assertThat(mockHttpServletResponse.getRedirectedUrl()).isEqualTo(noBearerUrl);
    }

    @Test
    public void testUpdateCartInvalidTokenRedirects(){
        String badToken = "Bearer invalid";

        try{
            cartController.updateCart(mockHttpServletResponse, badToken, user.getUsername(), cart);
        } catch (IOException e){
            e.printStackTrace();
        } catch (JwtException e){
            assertThat(mockHttpServletResponse.getRedirectedUrl()).isEqualTo(invalidAuthUrl);
        }
    }

    @Test
    public void testSaveCartItemRedirects(){
        try{
            cartController.saveCartItem(mockHttpServletResponse, "test string", user.getUsername(), cart);
        } catch (IOException e){
            e.printStackTrace();
        }

        assertThat(mockHttpServletResponse.getRedirectedUrl()).isEqualTo(noBearerUrl);
    }

    @Test
    public void testSaveCartItemInvalidTokenRedirects(){
        String badToken = "Bearer invalid";

        try{
            cartController.saveCartItem(mockHttpServletResponse, badToken, user.getUsername(), cart);
        } catch (IOException e){
            e.printStackTrace();
        } catch (JwtException e){
            assertThat(mockHttpServletResponse.getRedirectedUrl()).isEqualTo(invalidAuthUrl);
        }
    }

}
