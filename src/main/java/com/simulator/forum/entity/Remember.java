package com.simulator.forum.entity;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "remember")
public class Remember {
	
	@Id
	private String series;

	private String token;
	
	private String device;
	
	@Column( insertable = false)
	private ZonedDateTime lastUsed;
	
	private String  email;
	
	
	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public ZonedDateTime getLastUsed() {
		return lastUsed;
	}

	public void setLastUsed(ZonedDateTime lastUsed) {
		this.lastUsed = lastUsed;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
