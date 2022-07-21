package com.forest.forest.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name="carts")
/* From Bryan's Notes
@Data // generate getters/setter, toString, hashCode, and equals methods automatically
@NoArgsConstructor
@AllArgsConstructor
*/
public class Cart 
{
	@Id
	@Column(name="timeordered")
	private Timestamp timeStamp;
	
    @ManyToOne
    @JoinColumn(name="userid")
    private User user;
	
	@ManyToOne
	@JoinColumn(name="productid")
	private Product product;
	
	@Column(name="productcount")
	private int count;

	public Cart(Timestamp timeStamp, User user, Product product, int count) {
		super();
		this.timeStamp = timeStamp;
		this.user = user;
		this.product = product;
		this.count = count;
	}

	public Timestamp getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Timestamp timeStamp) {
		this.timeStamp = timeStamp;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "Cart [timeStamp=" + timeStamp + ", user=" + user + ", product=" + product + ", count=" + count + "]";
	}
	
	
}