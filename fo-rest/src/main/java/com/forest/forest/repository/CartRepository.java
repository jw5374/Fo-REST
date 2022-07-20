package com.forest.forest.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.forest.forest.models.Cart;
import com.forest.forest.models.Product;
import com.forest.forest.models.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>
{
	//Does this work??
	List<Product> FindByProductContaining(int price);
	
	/////////////////////////////////////////////////////////////
	//    --------------------Debugging--------------------    //
	/////////////////////////////////////////////////////////////
		// long id
	List<Product> findByIdIsNull();
		// List<User> userList
	List<Product> findByUserListIsNull();
		// List<Product> productList
	List<Product> findByProductListIsNull();
		// int count
	List<Product> findByCountIsNull();


}
