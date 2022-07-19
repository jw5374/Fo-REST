package com.forest.forest.entity;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;


@Entity
@Table(name="carts")
/* From Bryan's Notes
@Data // generate getters/setter, toString, hashCode, and equals methods automatically
@NoArgsConstructor
@AllArgsConstructor
*/
public class Cart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="orderId")
	private int id;
	
	//mapped by examples always have lowercase of the class name
	
	
    @OneToMany(mappedBy="userId", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> userList;
	
	@OneToMany(mappedBy="productId", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Product> productId;
	
	
	@Column(name="productCount")
	private int count;
	
	
	public Cart() {
		// TODO Auto-generated constructor stub
	}

	/////////////////////////////////////////////////////////////
	// -------------------GETTERS AND SETTERS------------------//
	/////////////////////////////////////////////////////////////

	public Cart(int id, List<User> userList, List<Product> productId, int count) {
		super();
		this.id = id;
		this.userList = userList;
		this.productId = productId;
		this.count = count;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public List<User> getUserList() {
		return userList;
	}


	public void setUserList(List<User> userList) {
		this.userList = userList;
	}


	public List<Product> getProductId() {
		return productId;
	}


	public void setProductId(List<Product> productId) {
		this.productId = productId;
	}


	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}
	
	
	
}