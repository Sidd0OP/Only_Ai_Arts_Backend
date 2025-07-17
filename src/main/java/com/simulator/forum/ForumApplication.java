package com.simulator.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.simulator.forum.search.Search;


@SpringBootApplication()
public class ForumApplication {

	public static void main(String[] args) {
		
		Search.initializeSearchPipeline();
		SpringApplication.run(ForumApplication.class, args);
	}

//	@Bean
//	public WebMvcConfigurer corsConfigurer() {
//		return new WebMvcConfigurer() {
//			@Override
//			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/home").allowedOrigins("http://localhost:5173");
//				
//			}
//		};
//	}
}
