package com.forest.forest.models;


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
	private long id;
	
	//mapped by examples always have lowercase of the class name
	
	
    @OneToMany
    @JoinColumn(name="userFk", referencedColumnName="orderId")
    private List<User> userList;
	
	@OneToMany
	@JoinColumn(name="productFk", referencedColumnName="orderId")
	private List<Product> productList;
	
	
	@Column(name="productCount")
	private int count;
	
	
	
	
	/////////////////////////////////////////////////////////////
	//    -------------------Constructors------------------    //
	/////////////////////////////////////////////////////////////	
	
	public Cart() {
		// TODO Auto-generated constructor stub
	}

	public Cart(long id, List<User> userList, List<Product> productList, int count) {
		super();
		this.id = id;
		this.userList = userList;
		this.productList = productList;
		this.count = count;
	}

	
	
	/////////////////////////////////////////////////////////////
	// -------------------GETTERS AND SETTERS------------------//
	/////////////////////////////////////////////////////////////

	// id
	
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}

	// userList

	public List<User> getUserList() {
		return userList;
	}


	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	// productList
	
	public List<Product> getProductList() {
		return productList;
	}


	public void setProductList(List<Product> productId) {
		this.productList = productId;
	}

	// count
	
	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}

	/////////////////////////////////////////////////////////////
	//    ---------------------toString--------------------    //
	/////////////////////////////////////////////////////////////	

	@Override
	public String toString() {
		return "Cart [id=" + id + ", userList=" + userList + ", productList=" + productList + ", count=" + count + "]";
	}
	
	
	
}