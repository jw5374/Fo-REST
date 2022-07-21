package com.forest.forest.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.forest.forest.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> 
{
	@Query(
		value = "SELECT * FROM products WHERE lower(name) LIKE %:nameSearch% OR lower(description) LIKE %:descSearch%",
		nativeQuery = true
	)
	public List<Product> findByNameOrDescriptionContaining(
		@Param("nameSearch") String name, 
		@Param("descSearch") String description
	);
	
	public Optional<Product> findById(long id);
}
