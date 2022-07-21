package com.forest.forest.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.forest.forest.models.Cart;
import com.forest.forest.models.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Timestamp>
{
	//Does this work??
	//public List<Product> FindByProductContaining(int price);
	
	public List<Cart> findByUser(User user);

}
