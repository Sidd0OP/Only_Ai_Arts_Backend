package com.simulator.forum.controller;

import java.security.SecureRandom;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.simulator.forum.cloudflare.MediaService;
import com.simulator.forum.entity.UserDetail;
import com.simulator.forum.model.RegisterForm;
import com.simulator.forum.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthenticationController {
	
	@Autowired
	private UserRepository userRepository;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@GetMapping("/login")
	public String login() 
	{
		return "login";
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(HttpServletRequest request , @ModelAttribute RegisterForm details) 
	{
		String email = details.getEmail();
		
		String password = details.getPassword();
		
		if(!validEmail(email)) 
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if(!newEmail(email)) 
		{
			return new ResponseEntity<>("Already Exists"  , HttpStatus.FORBIDDEN);
		}
		
		if(!validPassword(password)) 
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		
		String salt = generateSalt();
		
		String hash = hash(password , salt);
		
		String remoteAddr = request.getRemoteAddr();
		
		userRepository.createUser(email, remoteAddr, remoteAddr, salt, hash);	
		
		return new ResponseEntity<>("Created"  , HttpStatus.OK);
	}

	private boolean validEmail(String email) 
	{
		Pattern emailRegexPatter = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
		return emailRegexPatter.matcher(email).matches();
	}
	
	private boolean validPassword(String password) 
	{
		/*
		 * - Has minimum 8 characters in length
		 * - At least one upperCase English letter.
		 * - At least one lowerCase English letter. 
		 * - At least one digit.
		 * - At least one special character
		 * 
		 */
		Pattern passwordRegexPatter = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$");
		return passwordRegexPatter.matcher(password).matches();
	}
	
	private boolean newEmail(String email) 
	{
		return !userRepository.emailExist(email);
	}
	
	private String generateSalt() {
		
		byte[] salt = new byte[64];
		
		SecureRandom random = new SecureRandom();
		
		random.nextBytes(salt);
		
		return String.valueOf(salt);
	}
	
	private String hash(String password , String salt) 
	{
		return passwordEncoder.encode(password + salt);
	}
	
	
	
	

}
