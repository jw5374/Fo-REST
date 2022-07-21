package com.forest.forest.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.forest.forest.models.Product;
import com.forest.forest.repository.ProductRepository;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductRepository prodRepo;

    @ResponseBody
    @GetMapping("/product/{prodid}")
    public Product getProductById(@PathVariable("prodid") long id) {
        Optional<Product> prod = prodRepo.findById(id);
        if(prod.isPresent()) {
            return prod.get();
        }
        return null;
    }

    @ResponseBody
    @GetMapping
    public List<Product> getAllProducts() {
        return prodRepo.findAll();
    }

    @ResponseBody
    @GetMapping("/product")
    public List<Product> searchProduct(@RequestParam("q") String search) {
        search = search.toLowerCase();
        return prodRepo.findByNameOrDescriptionContaining(search, search);
    }
}
