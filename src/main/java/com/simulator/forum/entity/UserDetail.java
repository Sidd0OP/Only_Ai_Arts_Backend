package com.simulator.forum.entity;

import java.time.ZonedDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_detail")
@Getter
@Setter
public class UserDetail {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	
	private long id;
	
	private String email;

	private ZonedDateTime created;
	
	private String name;
	
	private String profilePhotoUrl;
	
	private String bio;
	
	private ZonedDateTime lastLoginTime;
	
	private ZonedDateTime currentSignInTime;
	
	private Integer signInCount;
	
	private String lastLoginIp;
	
	private String currentSignInIp;
	
	private String salt;
	
	private String password;
	
	@Override
	public String toString() 
	{
		return this.email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
