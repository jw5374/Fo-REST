package com.forest.forest.controllers;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.forest.forest.models.Product;
import com.forest.forest.repository.ProductRepository;
import com.forest.forest.service.ProductService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductControllerTests {

    @InjectMocks
    private ProductController productController;

    @MockBean
    private ProductService productService;

    private Product product;

    @BeforeEach
    public void init(){
        product = new Product(1, "Test Product", "Test Description", 4, 2);
    }

    @AfterEach
    public void teardown(){
        product = null;
    }

    @Test
    public void testGetProductByIdCallsProductService(){
        when(productService.findById(1)).thenReturn(product);

        assertThat(productController.getProductById(1)).isEqualTo(product);
    }

    @Test
    public void testAllGetProductsCallsProductService(){
        List<Product> products = new ArrayList<>();

        products.add(product);
        products.add(product);
        products.add(product);

        when(productService.findAll()).thenReturn(products);

        assertThat(productController.getAllProducts()).isEqualTo(products);
    }

    @Test
    public void testSearchProductsCallsProductService(){
        List<Product> products = new ArrayList<>();

        products.add(product);
        products.add(product);
        products.add(product);

        when(productService.findByNameOrDescriptionContaining("search")).thenReturn(products);

        assertThat(productController.searchProduct("search")).isEqualTo(products);
    }

    @Test
    public void testRandom8CallsProductService(){
        List<Product> products = new ArrayList<>();

        products.add(product);
        products.add(product);
        products.add(product);

        when(productService.random8Prod()).thenReturn(products);

        assertThat(productController.random8()).isEqualTo(products);
    }
}