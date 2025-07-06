package com.simulator.forum.controller;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.simulator.forum.dto.snippet.HomeDto;
import com.simulator.forum.dto.snippet.HomePostSnippet;
import com.simulator.forum.entity.UserDetail;
import com.simulator.forum.repository.HeartRepository;
import com.simulator.forum.repository.PostRepository;
import com.simulator.forum.repository.TagRepository;
import com.simulator.forum.repository.UserRepository;


@RestController
public class HomeController {
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private HeartRepository heartRepository;
	
	@Autowired
	private TagRepository tagRepository;
	
	
	private UserDetail findUserFromSession() 
	{
		Authentication  authObject  = SecurityContextHolder.getContext().getAuthentication();
			
		
		if(authObject != null && authObject.isAuthenticated()) 
		{
			UserDetail authenticatedUser = userRepository.findByEmail(authObject.getName());
			
			return authenticatedUser;
			
		}else {return null;}
	}

	@GetMapping("/home")
	public ResponseEntity<?> home() 
	{	
		
		UserDetail user =  findUserFromSession();
		
		List<Long> heartedPosts = List.of();
		
		
		if(user != null) 
		{
			long userId = user.getId();
			heartedPosts = heartRepository.getHeartedPost(userId);
		}
		
		
		
		HomeDto homeData = new HomeDto(	postRepository.getTrendingTool(),
										tagRepository.getTrendingTags() ,
										heartedPosts , 
										postRepository.getLatestPostSnippets(), 
										postRepository.getPostSnippets(0));
		
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
	
	
	@GetMapping("/tags/{tag}/{page}")
	public ResponseEntity<?> loadPostFromTags(@PathVariable String tag , @PathVariable String page)
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
		
		return new ResponseEntity<>(postRepository.getPostSnippetsFromTag(tag ,pageNumber) , HttpStatus.OK);
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
		
		return new ResponseEntity<>(postRepository.getPostSnippets(pageNumber) , HttpStatus.OK);
	}
	
	
	@GetMapping("/")
	public String main() 
	{
		return "Home";
	}
	
	
	
}
