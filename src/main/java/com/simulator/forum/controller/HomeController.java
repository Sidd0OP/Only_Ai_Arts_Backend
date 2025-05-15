package com.simulator.forum.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	@GetMapping("/profile")
	public String profile() 
	{
		return "User Profile";
	}

	@GetMapping("/home")
	public String home() 
	{
		return "home";
	}
	
	
	@PostMapping("/post")
	public String post() 
	{
		return "Post Page";
	}
}
