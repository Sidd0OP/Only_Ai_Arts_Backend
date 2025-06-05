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
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private ReplyRepository replyRepository;
	
	

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
	
	@GetMapping("/profile/{userId}")
	public ResponseEntity<?> profile(@PathVariable long userId) 
	{

		
		Optional<UserDetail> user = userRepository.findById(userId);
		
		if(user.isEmpty()) {return new ResponseEntity<>("User not found"  , HttpStatus.NOT_FOUND);}
		
		List<UserPostSnippet> posts = postRepository.findAllByUserId(userId).stream()
				.map(p -> new UserPostSnippet(p.getUserId() , 
						p.getTitle() , 
						p.getBody() , 
						safeInstant(p.getCreated()),
						safeInstant(p.getEdited()) , 
						p.getCommentCount())
						).toList();
		
		
		List<CommentSnippet> comments = commentRepository.findAllByUserId(userId).stream()
				.map(c -> new CommentSnippet(
						c.getPostId() , 
						c.getBody() , 
						safeInstant(c.getCreated()),
						safeInstant(c.getEdited()) , 
						c.getEditCount())
						).toList();
		
		
		List<ReplySnippet> replies = replyRepository.findAllByUserId(userId).stream()
				.map(r -> new ReplySnippet(
						r.getPostId(),
						r.getBody(),
						safeInstant(r.getCreated()),
						safeInstant(r.getEdited()) 
						)
					).toList();
		
		
		
		
		UserProfileDto userProfile = new UserProfileDto(
				
				user.get().getId() , 
				user.get().getName() , 
				user.get().getProfilePhotoUrl(),
				safeInstant(user.get().getCreated()),
				user.get().getBio(),
				posts,
				comments,
				replies
				

				);
		
		
		
		
		return new ResponseEntity<>(userProfile	, HttpStatus.OK);
	}
	
	
	private Instant safeInstant(ZonedDateTime date) {
	    return date != null ? date.toInstant() : null;
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
