package com.forest.forest.repository;


import java.util.List;
import java.util.Optional;

import javax.persistence.Column;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.forest.forest.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> 
{
	public List<Product> findByNameOrDescriptionContaining(String name, String description);
	public Optional<Product> findById(long id);
}
