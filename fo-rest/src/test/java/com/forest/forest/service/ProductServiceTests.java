package com.forest.forest.service;

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

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

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
    public void testFindByIdReturnsProduct(){
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        assertThat(productService.findById(1)).isEqualTo(product);
    }

    @Test
    public void testFindByIdReturnsNullIfNotFound(){
        when(productRepository.findById(1)).thenReturn(Optional.ofNullable(null));

        assertNull(productService.findById(1));
    }

    @Test
    public void testFindAllReturnsList(){
        List<Product> products = new ArrayList<>();
        
        products.add(product);
        products.add(new Product(2, "Test Product 2", "Test Description 2", 6, 9));
        products.add(new Product(3, "Test Product 3", "Test Description 3", 8, 12));

        when(productRepository.findAll()).thenReturn(products);

        assertThat(productService.findAll()).isEqualTo(products);
    }

    @Test
    public void testFindByNameOrDescriptionContainingReturnsList(){
        List<Product> products = new ArrayList<>();
        
        products.add(product);
        products.add(new Product(2, "Test Product 2", "Test Description 2", 6, 9));
        products.add(new Product(3, "Test Product 3", "Test Description 3", 8, 12));

        when(productRepository.findByNameOrDescriptionContaining("test", "test")).thenReturn(products);

        assertThat(productService.findByNameOrDescriptionContaining("test")).isEqualTo(products);
    }

    @Test
    public void random8ProdReturnsListOfSize8(){
        List<Product> products = new ArrayList<>();
        
        products.add(product);
        products.add(new Product(2, "Test Product 2", "Test Description 2", 6, 9));
        products.add(new Product(3, "Test Product 3", "Test Description 3", 8, 12));
        products.add(new Product(4, "Test Product 4", "Test Description 4", 8, 12));
        products.add(new Product(5, "Test Product 5", "Test Description 5", 8, 12));
        products.add(new Product(6, "Test Product 6", "Test Description 6", 8, 12));
        products.add(new Product(7, "Test Product 7", "Test Description 7", 8, 12));
        products.add(new Product(8, "Test Product 8", "Test Description 8", 8, 12));
        products.add(new Product(9, "Test Product 9", "Test Description 9", 8, 12));
        products.add(new Product(10, "Test Product 10", "Test Description 10", 8, 12));
        products.add(new Product(11, "Test Product 11", "Test Description 11", 8, 12));
        products.add(new Product(12, "Test Product 12", "Test Description 12", 8, 12));

        when(productRepository.findAll()).thenReturn(products);

        assertThat(productService.random8Prod().size()).isEqualTo(8);
    }

    @Test
    public void testUpdateProductStockReturnsInt(){
        when(productRepository.updateProductStock(3,1)).thenReturn(3);

        assertThat(productService.updateProductStock(3, 1)).isEqualTo(3);
    }
}