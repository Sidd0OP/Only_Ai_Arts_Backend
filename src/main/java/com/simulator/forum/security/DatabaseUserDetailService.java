package com.simulator.forum.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.simulator.forum.entity.UserDetail;
import com.simulator.forum.repository.UserRepository;

@Service
public class DatabaseUserDetailService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		UserDetail user = userRepository.findByEmail(email);
		
		
		if(user == null) 
		{
			throw new UsernameNotFoundException("Email Not Found: " + email);
		}
		
		return new UserStateDetails(user);
	}

}
