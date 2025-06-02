package com.simulator.forum.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity(debug = false)
public class WebSecurityConfig {

	@Autowired
	private DatabaseUserDetailService databaseUserDetailService;
	
	@Autowired 
	private RememberMeService rememberMeService;
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http.authorizeHttpRequests(auth -> {
			auth
			.requestMatchers(
					"/register" , 
					"/home" , 
					"/" , 
					"/error" , 
					"/post/**" , 
					"/snippets/**" , 
					"/profile/**").permitAll()
			
			.requestMatchers(
					"/create" , 
					"/me" , 
					"/new/**" , 
					"/comment/**" , 
					"/reply/**",
					"/upload/profile").authenticated();
		})
		
		.csrf(c -> {
			c.disable();
		})
		
//		.formLogin(form ->
//		{
//			form
//			.loginPage("/login")
//			.permitAll();
//		})
		
		.formLogin(Customizer.withDefaults())
		
		.logout(logout -> 
		{
			logout
			.permitAll();
			
		})
		
		.rememberMe(remember -> 
		
		remember
		.tokenRepository(rememberMeService)
		.tokenValiditySeconds(1209600)
		
		)
		
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
