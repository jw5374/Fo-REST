package com.forest.forest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.forest.forest.models.Product;
import com.forest.forest.service.ProductService;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductService prodServ;

    @ResponseBody
    @GetMapping("/product/{prodid}")
    public Product getProductById(@PathVariable("prodid") long id) {
        return prodServ.findById(id);
    }

    @ResponseBody
    @GetMapping
    public List<Product> getAllProducts() {
        return prodServ.findAll();
    }

    @ResponseBody
    @GetMapping("/product")
    public List<Product> searchProduct(@RequestParam("q") String search) {
        return prodServ.findByNameOrDescriptionContaining(search);
    }

    @ResponseBody
    @GetMapping("/random")
    public List<Product> random8() {
        return prodServ.random8Prod();
    }
}
