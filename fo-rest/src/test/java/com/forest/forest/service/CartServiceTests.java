package com.forest.forest.service;

import static org.mockito.Mockito.when;

import java.util.List;
import java.sql.Timestamp;
import java.util.ArrayList;

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

import com.forest.forest.models.Cart;
import com.forest.forest.models.Product;
import com.forest.forest.models.User;
import com.forest.forest.repository.CartRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CartServiceTests {

    @InjectMocks
    private CartService cartService;

    @MockBean
    private CartRepository cartRepository;

    private Cart cart;
    private User user;
    private Product product;

    @BeforeEach
    public void init(){
        product = new Product(1, "Test Product", "Test Description", 4, 2);
        user = new User("username", "password", "user@gmail.com", "123 Test Avenue");
        cart = new Cart(3, new Timestamp(3), user, product, 3);
    }

    @AfterEach
    public void teardown(){
        product = null;
        user = null;
        cart = null;
    }

    @Test
    public void testFindAllReturnsList(){
        List<Cart> carts = new ArrayList<>();
        
        carts.add(cart);
        carts.add(cart);
        carts.add(cart);

        when(cartRepository.findAll()).thenReturn(carts);

        assertThat(cartService.findAll()).isEqualTo(carts);
    }

    @Test
    public void testFindByUsernameReturnsList(){
        List<Cart> carts = new ArrayList<>();
        
        carts.add(cart);
        carts.add(cart);
        carts.add(cart);

        when(cartRepository.findByUser(user)).thenReturn(carts);

        assertThat(cartService.findByUsername(user)).isEqualTo(carts);
    }

    @Test
    public void testDeleteCartReturnsTrue(){
        //doNothing().when(cartRepository.delete(cart));

        assertTrue(cartService.deleteCart(cart));
    }

    @Test
    public void testUpdateCartCountReturnsCount(){
        when(cartRepository.updateCartCount(1, 2)).thenReturn(1);

        assertThat(cartService.updateCartCount(1, 2)).isEqualTo(1);
    }

    @Test
    public void testFindByUsernameAndProductReturnsCart(){
        when(cartRepository.findByUserAndProduct("username", 1)).thenReturn(cart);

        assertThat(cartService.findByUsernameAndProduct("username", 1)).isEqualTo(cart);
    }

    @Test
    public void testNewCartItemReturnsCart(){
        when(cartRepository.save(cart)).thenReturn(cart);

        assertThat(cartService.newCartItem(cart)).isEqualTo(cart);
    }
}