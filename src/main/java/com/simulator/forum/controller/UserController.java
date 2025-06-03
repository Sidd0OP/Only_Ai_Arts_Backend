package com.simulator.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.simulator.forum.cloudflare.MediaService;
import com.simulator.forum.entity.UserDetail;
import com.simulator.forum.repository.UserRepository;

@RestController
public class UserController {
	
	@Autowired
	MediaService mediaSerive;
	
	@Autowired
	private UserRepository userRepository;
	
	
	private UserDetail findUserFromSession() 
	{
		Authentication  authObject  = SecurityContextHolder.getContext().getAuthentication();
			
		
		if(authObject != null && authObject.isAuthenticated()) 
		{
			UserDetail authenticatedUser = userRepository.findByEmail(authObject.getName());
			
			return authenticatedUser;
			
		}else {return null;}
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
