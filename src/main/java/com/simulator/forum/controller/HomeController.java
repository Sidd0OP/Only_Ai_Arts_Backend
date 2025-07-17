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

import com.simulator.forum.dto.HomeDto;
import com.simulator.forum.dto.snippet.PostSnippet;
import com.simulator.forum.entity.UserDetail;
import com.simulator.forum.repository.HeartRepository;
import com.simulator.forum.repository.PostRepository;
import com.simulator.forum.repository.TagRepository;
import com.simulator.forum.repository.UserRepository;
import com.simulator.forum.search.Search;


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
	
	
	Search search = new Search();
	
	
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
		
		long userId = -1;
		
		if(user != null) 
		{
			userId = user.getId();
			heartedPosts = heartRepository.getHeartedPost(userId);
		}
		
		
		
		HomeDto homeData = new HomeDto(	postRepository.getTrendingTool(),
										tagRepository.getTrendingTags() ,
										heartedPosts , 
										postRepository.getLatestPostSnippets(0 , userId), 
										postRepository.getPostSnippets(0 , userId));
		
		return new ResponseEntity<>(homeData  , HttpStatus.OK);
	}
	
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable String query)
	{
		
//		search.keyphrase(query);
		List<PostSnippet> searchResult;
		
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
		
		UserDetail user =  findUserFromSession();
		
		
		long userId = -1;
		
		if(user != null) 
		{
			userId = user.getId();
			
		}
		
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
		
		return new ResponseEntity<>(postRepository.getPostSnippetsFromTag(pageNumber , tag , userId) , HttpStatus.OK);
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
		
		
		
		UserDetail user =  findUserFromSession();
				
		long userId = -1;
		
		if(user != null) 
		{
			userId = user.getId();
			
		}
		
		return new ResponseEntity<>(postRepository.getPostSnippets(pageNumber , userId) , HttpStatus.OK);
	}
	
	
	@GetMapping("/")
	public String main() 
	{
		return "Home";
	}
	
	
	
}
