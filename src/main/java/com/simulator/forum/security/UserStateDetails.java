package com.simulator.forum.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.simulator.forum.entity.UserDetail;

public class UserStateDetails implements UserDetails{

	private static final long serialVersionUID = 1L;
	
	
	private UserDetail user;

	public UserStateDetails(UserDetail user)
	{
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return Collections.singleton(new SimpleGrantedAuthority("USER"));
	}

	@Override
	public String getPassword() {
		
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		
		
		return user.getEmail();
	}
	
	public String getSalt() 
	{
		return user.getSalt();
	}

}
