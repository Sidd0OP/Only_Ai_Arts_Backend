package com.simulator.forum.controller;


import java.security.Principal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulator.forum.dto.UserProfileDto;
import com.simulator.forum.dto.snippet.CommentSnippet;
import com.simulator.forum.dto.snippet.HomeDto;
import com.simulator.forum.dto.snippet.HomePostSnippet;
import com.simulator.forum.dto.snippet.ReplySnippet;
import com.simulator.forum.dto.snippet.UserPostSnippet;
import com.simulator.forum.entity.Comment;
import com.simulator.forum.entity.Post;
import com.simulator.forum.entity.Reply;
import com.simulator.forum.entity.UserDetail;
import com.simulator.forum.repository.CommentRepository;
import com.simulator.forum.repository.PostRepository;
import com.simulator.forum.repository.ReplyRepository;
import com.simulator.forum.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HomeController {
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	
	

	@GetMapping("/home")
	public ResponseEntity<?> home() 
	{	
		HomeDto homeData = new HomeDto(postRepository.getLatestPostSnippets(), postRepository.getPostSnippets(0));
		
		return new ResponseEntity<>(homeData  , HttpStatus.OK);
	}
	
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable String query)
	{
		List<HomePostSnippet> searchResult;
		
		try {
			
			searchResult = postRepository.searchPost(query);
			
			
			
		}catch(Exception e) {
			
		
			searchResult = List.of();
		}
		
		
		return new ResponseEntity<>(searchResult  , HttpStatus.OK);
	}
	
	@GetMapping("/me")
	public String myProfile() 
	{
			
		UserDetail user;
		
		Authentication  authObject  = SecurityContextHolder.getContext().getAuthentication();
		
		if(authObject != null && authObject.isAuthenticated()) 
		{
			user = userRepository.findByEmail(authObject.getName());
			
			if(user == null) 
			{
				return "redirect:/error";
				
			}else {
				
				return "redirect:/profile/" + user.getId();
			}
		}
		
		return "redirect:/error";
	}
	
	
	
	
	@GetMapping("/snippets/{page}")
	public ResponseEntity<?> loadMore(@PathVariable String page)
	{
		if(page == null) 
		{
			return new ResponseEntity<>("format = snippets/page"  , HttpStatus.BAD_REQUEST);
		}
		
		int pageNumber;
		
		try 
		{
			pageNumber = Integer.valueOf(page) * 20;
			
		}catch(NumberFormatException e) {
			
			return new ResponseEntity<>("numeric parameter required"  , HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(postRepository.getPostSnippets(pageNumber)  , HttpStatus.OK);
	}
	
	
	@GetMapping("/")
	public String main() 
	{
		return "Home";
	}
	
	
	
}
