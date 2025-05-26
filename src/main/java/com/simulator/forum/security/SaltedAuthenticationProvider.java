package com.simulator.forum.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

public class SaltedAuthenticationProvider implements AuthenticationProvider{

	private DatabaseUserDetailService databaseUserDetailService;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	
	
	public SaltedAuthenticationProvider(DatabaseUserDetailService databaseUserDetailService) {
		this.databaseUserDetailService = databaseUserDetailService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		//user details provided by the web forum
		String email = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		
		
		UserStateDetails user  = (UserStateDetails) databaseUserDetailService.loadUserByUsername(email);
			
		
		
		String saltedPassword = passwordEncoder.encode(password + user.getSalt());
		
		
		String saltedDatabasePassword = user.getPassword();
		
		
		
		if(!passwordEncoder.matches(password + user.getSalt(), saltedDatabasePassword)) 
		{
			System.out.println("PASSWORDS DONT MATCH EXCEPTION WILL BE THROWN");
			throw new AuthenticationException("Password do not match") {
				private static final long serialVersionUID = 1L;};
		}
		
		
		Authentication authenticationObject = new UsernamePasswordAuthenticationToken(user , saltedPassword , user.getAuthorities());
		
		System.out.println("USER IS EXISTING AND IS AUTHENTICATED");
		return authenticationObject;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
