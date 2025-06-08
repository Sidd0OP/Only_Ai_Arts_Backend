package com.simulator.forum.controller;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.simulator.forum.cloudflare.MediaService;
import com.simulator.forum.dto.UserLoginDto;
import com.simulator.forum.dto.UserProfileDto;
import com.simulator.forum.dto.snippet.CommentSnippet;
import com.simulator.forum.dto.snippet.ReplySnippet;
import com.simulator.forum.dto.snippet.UserPostSnippet;
import com.simulator.forum.entity.UserDetail;
import com.simulator.forum.repository.CommentRepository;
import com.simulator.forum.repository.PostRepository;
import com.simulator.forum.repository.ReplyRepository;
import com.simulator.forum.repository.UserRepository;

@RestController
public class UserController {
	
	@Autowired
	MediaService mediaSerive;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private ReplyRepository replyRepository;
	
	
	private UserDetail findUserFromSession() 
	{
		Authentication  authObject  = SecurityContextHolder.getContext().getAuthentication();
			
		
		if(authObject != null && authObject.isAuthenticated()) 
		{
			UserDetail authenticatedUser = userRepository.findByEmail(authObject.getName());
			
			return authenticatedUser;
			
		}else {return null;}
	}
	
	
	@GetMapping("/user")
	public ResponseEntity<?> getUserInfo()
	{
		UserDetail user =  findUserFromSession();
		
		if(user == null) 
		{
			UserLoginDto userData = new UserLoginDto(null , null , null);
			
			return new ResponseEntity<>(userData , HttpStatus.FORBIDDEN);
		}
		
		UserLoginDto userData = new UserLoginDto(user.getId() , user.getName() , user.getProfilePhotoUrl());
		
		return new ResponseEntity<>(userData  , HttpStatus.OK);
	}
	
	@GetMapping("/profile/{userId}")
	public ResponseEntity<?> profile(@PathVariable long userId) 
	{
		UserDetail userDetail =  findUserFromSession();
		
		boolean dataEditable =  false;
			
		if(userDetail != null) 
		{
			if(userId == userDetail.getId()) {
				dataEditable = true;
			}		
			
		}

		
		Optional<UserDetail> user = userRepository.findById(userId);
		
		if(user.isEmpty()) {return new ResponseEntity<>("User not found"  , HttpStatus.NOT_FOUND);}
		
		List<UserPostSnippet> posts = postRepository.findAllByUserId(userId).stream()
				.map(p -> new UserPostSnippet(p.getUserId() , 
						p.getTitle() , 
						p.getBody() , 
						safeInstant(p.getCreated()),
						safeInstant(p.getEdited()) , 
						p.getImageUrl())
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
				dataEditable,
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
	
	
	
	@PostMapping("/upload/profile")
	public ResponseEntity<?> uploadProfilePhoto(@RequestParam(value = "file" , required = true) MultipartFile file) 
	{
			UserDetail user =  findUserFromSession();
			
			if(user == null) 
			{
				return new ResponseEntity<>("Not a user" , HttpStatus.FORBIDDEN);
			}
		
			try 
			{
				String url = mediaSerive.uploadFile(file , "image");
				user.setProfilePhotoUrl(url);
				userRepository.save(user);
				return new ResponseEntity<>("Created"  , HttpStatus.OK);
				
			}catch(Exception e) 
			{
				return new ResponseEntity<>("Failed"  , HttpStatus.BAD_REQUEST);
			}
		
	}

}
