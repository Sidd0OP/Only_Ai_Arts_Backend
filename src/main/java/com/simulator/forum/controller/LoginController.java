package com.simulator.forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulator.forum.entity.UserDetail;
import com.simulator.forum.repository.UserRepository;

@RestController
public class LoginController {
	
	@Autowired
	private UserRepository userRepository;
	
	
	@GetMapping("/new")
	public String login() 
	{
		return "Login";
	}
	

}
