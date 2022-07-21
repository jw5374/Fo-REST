package com.forest.forest.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "pass")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "shippingaddress")
    private String shippingAddress;
    
    // "user" reference in cart
	@OneToMany(mappedBy = "user") 
	private List<Cart> userList;

    public User(){
        
    }
    
    public User(String username, String password, String email, String shippingAddress){
        this.username = username;
        this.password = password;
        this.email = email;
        this.shippingAddress = shippingAddress;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }    

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

	public List<Cart> getUserList() {
		return userList;
	}

	public void setUserList(List<Cart> userList) {
		this.userList = userList;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", email=" + email + ", shippingAddress="
				+ shippingAddress + ", userList=" + userList + "]";
	}
    
}
