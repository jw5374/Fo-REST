package com.forest.forest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.forest.forest.models.Cart;
import com.forest.forest.models.User;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer>
{

	public List<Cart> findByUser(User user);

	@Query(value = "UPDATE carts SET productcount = :newcount WHERE cartid = :id RETURNING carts.productcount", nativeQuery = true)
	public int updateCartCount(
		@Param("newcount") int newcount,
		@Param("id") int id
	);

	@Query(value = "SELECT * FROM carts WHERE userid = :uname AND productid = :prodid", nativeQuery = true)
	public Cart findByUserAndProduct(
		@Param("uname") String username,
		@Param("prodid") long prodId
	);

}
