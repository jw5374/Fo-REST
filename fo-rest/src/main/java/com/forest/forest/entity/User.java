package com.forest.forest.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name="users")
/* From Bryan's Notes
@Data // generate getters/setter, toString, hashCode, and equals methods automatically
@NoArgsConstructor
@AllArgsConstructor
*/
public class User {

	@Id
	@JoinColumn(name="userId")
	private String username;
	
	@Column(name = "pass")
	private String pass;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "shipping_address")
	private String shippingAddress;
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(String username, String pass, String email, String shippinAddress)
	{
		this.username =  username;
		this.pass = pass;
		this.email = email;
		this.shippingAddress=shippinAddress;
	}

	/////////////////////////////////////////////////////////////
	// -------------------GETTERS AND SETTERS------------------//
	/////////////////////////////////////////////////////////////
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(String shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

	
	
}
