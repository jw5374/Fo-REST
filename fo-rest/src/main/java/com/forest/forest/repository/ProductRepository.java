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
	
	/////////////////////////////////////////////////////////////
	//    ----------------------Count----------------------    //
	/////////////////////////////////////////////////////////////
		// For re-ordering on the page
	List<Product> findByCountLessThan(int count);
	List<Product> findByCountGreaterThan(int count);
	List<Product> findTop5ByCount(int count);
	
	
	
	
	/////////////////////////////////////////////////////////////
	//    ----------------------Price----------------------    //
	/////////////////////////////////////////////////////////////
		// For re-ordering our products on the page
	List<Product> findByPriceLessThan(int price);
	List<Product> findByPriceGreatherThan(int price);
	List<Product> findTop5ByPrice(int price);
		// For specific name queries.
	List<Product> findByNameEquals(String name);
	
	
	
	
	/////////////////////////////////////////////////////////////
	//    --------------------Debugging--------------------    //
	/////////////////////////////////////////////////////////////
		// long id;
	List<Product> findByIdIsNull();
		// String name;
	List<Product> findByNameIsNull();
		// String description;
	List<Product> findByDescriptionNull();
		// int price;
	List<Product> findByPriceIsNull();
		// int count;
	List<Product> findByCountIsNull();

}
