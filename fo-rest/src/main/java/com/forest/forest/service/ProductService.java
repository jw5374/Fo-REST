package com.forest.forest.service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.forest.forest.models.Product;
import com.forest.forest.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public Product findById(long id) {
        Optional<Product> prod = productRepository.findById(id);
        if(prod.isPresent()) {
            return prod.get();
        }
        return null;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findByNameOrDescriptionContaining(String search) {
        search = search.toLowerCase();
        return productRepository.findByNameOrDescriptionContaining(search, search);
    }

    public List<Product> random8Prod() {
        List<Product> allProd = productRepository.findAll();
        Random rand = new Random();
        int randIndex = rand.nextInt(allProd.size() - 8);

        return allProd.subList(randIndex, randIndex + 8);
    }

    public int updateProductStock(int count, long id) {
        return productRepository.updateProductStock(count, id);
    }
}