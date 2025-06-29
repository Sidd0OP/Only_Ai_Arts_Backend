package com.simulator.forum.security;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity(debug = false)
public class WebSecurityConfig {

	@Autowired
	private DatabaseUserDetailService databaseUserDetailService;
	
	@Autowired 
	private CustomTokenRepository rememberMeRepositoryService;
	
	@Autowired
	private RememberMeService rememberMeService;
	
	@Bean 
	public CorsConfigurationSource CorsConfiguration() 
	{
		CorsConfiguration config = new CorsConfiguration();
		
		config.setAllowedOrigins(List.of("http://localhost:5173","https://onlyaiarts.com"));
		config.setAllowedMethods(List.of("GET" , "POST" , "PATCH"));
		config.setAllowedHeaders(List.of("*"));
		config.setAllowCredentials(true);

		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", config);
	    return source;
	}
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http.authorizeHttpRequests(auth -> {
			auth
			.requestMatchers(
					"/login",
					"/register" , 
					"/home" , 
					"/" , 
					"/error" , 
					"/post/**" , 
					"/snippets/**" , 
					"/profile/**",
					"/user/**",
					"/search/**",
					"/auth/google/**",
					"/token/**",
					"/valid/**",
					"/gallery/**").permitAll()
			
			.requestMatchers(
					"/create" , 
					"/me" , 
					"/new/**" , 
					"/comment/**" , 
					"/reply/**",
					"/upload/profile",
					"/heart/**",
					"/bio/**").authenticated();
		})
		.cors(c -> c.configurationSource(CorsConfiguration()))
		
		.csrf(c -> {
			c.disable();
		})
		
		.formLogin(form ->
		{
			form
			.loginPage("/login")
			.permitAll()
			.defaultSuccessUrl("/user", true);
			
		})
		
//		.formLogin(Customizer.withDefaults())
		
		.logout(logout -> 
		{
			logout
			.permitAll();
			
		})
		
		.rememberMe(remember -> 
		
		remember
		.userDetailsService(databaseUserDetailService)
		.tokenRepository(rememberMeRepositoryService)
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
