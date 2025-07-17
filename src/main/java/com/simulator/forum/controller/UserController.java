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
			
			return new ResponseEntity<>(userData , HttpStatus.NOT_FOUND);
		}
		
		UserLoginDto userData = new UserLoginDto(user.getId() , user.getName() , user.getProfilePhotoUrl());
		
		return new ResponseEntity<>(userData  , HttpStatus.OK);
	}
	
	@GetMapping("/profile/{userId}")
	public ResponseEntity<?> profile(@PathVariable long userId) 
	{
		UserDetail userDetail =  findUserFromSession();
		
		long loggedInUserId = -1;
		boolean profileEditable = false;
			
		if(userDetail != null) 
		{
			loggedInUserId = userDetail.getId();	
			
			if(loggedInUserId == userId) 
			{
				profileEditable = true;
			}
			
		}
		
		
		
		Optional<UserDetail> user = userRepository.findById(userId);
		
		if(user.isEmpty()) 
		{
			return new ResponseEntity<>("User not found"	, HttpStatus.OK);
		}
		
		
		String userName = user.get().getName();
		String profilePhotoUrl = user.get().getProfilePhotoUrl();
		
		if(user.isEmpty()) {return new ResponseEntity<>("User not found"  , HttpStatus.NOT_FOUND);}
		
		List<UserPostSnippet> posts = postRepository.findAllByUserId(userId).stream()
				.map(p -> new UserPostSnippet(
						p.getId() , 
						p.getTitle() , 
						p.getBody() , 
						safeInstant(p.getCreated()),
						safeInstant(p.getEdited()) , 
						p.getImageUrl())
						).toList();
		
		
		List<CommentSnippet> comments = commentRepository.findAllByUserId(userId).stream()
				.map(c -> new CommentSnippet(
						c.getId() ,
						c.getUserId(),
						c.getBody() , 
						safeInstant(c.getCreated()),
						safeInstant(c.getEdited()) , 
						userName,
						profilePhotoUrl,
						true)
						).toList();
		
		
		List<ReplySnippet> replies = replyRepository.findAllByUserId(userId).stream()
				.map(r -> new ReplySnippet(
						r.getId(),
						r.getUserId(),
						r.getCommentId(),
						r.getBody(),
						safeInstant(r.getCreated()),
						safeInstant(r.getEdited()),
						userName,
						profilePhotoUrl,
						true
						)
					).toList();
		
		
		
		
		UserProfileDto userProfile = new UserProfileDto(
				profileEditable,
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
	
	@PostMapping("/bio")
	public ResponseEntity<?> addBio(@RequestParam("bio") String bio)
	{
		UserDetail user =  findUserFromSession();
		
		if(user == null) 
		{
			return new ResponseEntity<>("Not a user" , HttpStatus.FORBIDDEN);
		}
		
		try 
		{
			
			user.setBio(bio);
			userRepository.save(user);
			
			return new ResponseEntity<>("Saved"  , HttpStatus.OK);
			
		}catch(Exception e) 
		{
			return new ResponseEntity<>("Failed"  , HttpStatus.BAD_REQUEST);
		}
	}

}
