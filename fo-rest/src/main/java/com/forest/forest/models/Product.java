package com.forest.forest.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="products")
/* From Bryan's Notes
@Data // generate getters/setter, toString, hashCode, and equals methods automatically
@NoArgsConstructor
@AllArgsConstructor
*/
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="description")
	private String description;
	
	@Column(name="price")
	private double price;
	
	@Column(name="count")
	private int count;

	@Column(name="images")
	private String imageList;
	
	// "product" reference in cart
	@OneToMany(mappedBy = "product")
	private List<Cart> cartList;
	
	
	/////////////////////////////////////////////////////////////
	//    -------------------Constructors------------------    //
	/////////////////////////////////////////////////////////////	
	
	public Product() {
		// TODO Auto-generated constructor stub
	}


	public Product(long id, String name, String description, int price, int count) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
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

	// name
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	// description

	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	// price
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	// count
	
	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}

	public String getImageList() {
		return imageList;
	}
	
	public void setImageList(String imageList) {
		this.imageList = imageList;
	}

	// cartList (relationship)
	
	public List<Cart> getCartList() {
		return cartList;
	}


	public void setCartList(List<Cart> cartList) {
		this.cartList = cartList;
	}

	/////////////////////////////////////////////////////////////
	//    ---------------------toString--------------------    //
	/////////////////////////////////////////////////////////////	


	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", description=" + description + ", price=" + price + ", count="
				+ count + "]";
	}

}
