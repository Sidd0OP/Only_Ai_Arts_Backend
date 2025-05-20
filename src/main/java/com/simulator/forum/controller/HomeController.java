package com.simulator.forum.controller;


import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulator.forum.dto.PostSnippet;
import com.simulator.forum.entity.Post;
import com.simulator.forum.entity.UserDetail;
import com.simulator.forum.model.PostSnippetModel;
import com.simulator.forum.repository.PostRepository;
import com.simulator.forum.repository.UserRepository;

@RestController
public class HomeController {
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	@GetMapping("/profile")
	public String profile() 
	{
		return "User Profile";
	}

	@GetMapping("/home")
	public ResponseEntity<?> home() 
	{		
		

		
		
		
		return new ResponseEntity<>(postRepository.getPostSnippets()  , HttpStatus.OK);
	}
	
	
	@PostMapping("/post")
	public String post() 
	{
		return "Post Page";
	}
}
