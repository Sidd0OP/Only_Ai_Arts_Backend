package com.simulator.forum.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig {

	@Autowired
	private DatabaseUserDetailService databaseUserDetailService;
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http.authorizeHttpRequests(auth -> {
			auth
			.requestMatchers("/register" , "/home" , "/").permitAll()
			.requestMatchers("/post").authenticated();
		})
		
		.csrf(c -> {
			c.disable();
		})
		
		.formLogin(form ->
		{
			form
			.loginPage("/new")
			.permitAll();
		})
		
		
		.logout(logout -> 
		{
			logout
			.permitAll();
			
		})
		
		.httpBasic();
		
		return http.build();
	}
	
	
	
	
	
	@Bean 
	public AuthenticationProvider authenticationProvider() 
	{
		SaltedAuthenticationProvider saltedAuthenticationProvider = new SaltedAuthenticationProvider(databaseUserDetailService);
		
		return saltedAuthenticationProvider;
	}
	
}
