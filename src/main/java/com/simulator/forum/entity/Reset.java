package com.simulator.forum.entity;

import java.time.ZonedDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reset")
public class Reset {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	private String email;
	
	private String token;
	
	public Reset(){}
	
	public Reset(long id, String email, String token, ZonedDateTime time) {

		this.id = id;
		this.email = email;
		this.token = token;
		this.time = time;
	}
	
	
	@Column(insertable = false)
	private ZonedDateTime time;
	
	public Reset(String email, String token) {
		
		this.email = email;
		this.token = token;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public ZonedDateTime getTime() {
		return time;
	}

	public void setTime(ZonedDateTime time) {
		this.time = time;
	}

}
