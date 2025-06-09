package com.simulator.forum.controller;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.simulator.forum.cloudflare.MediaService;
import com.simulator.forum.entity.Reset;
import com.simulator.forum.entity.UserDetail;
import com.simulator.forum.mail.EmailSender;
import com.simulator.forum.model.RegisterForm;
import com.simulator.forum.model.update.PasswordUpdateForm;
import com.simulator.forum.repository.ResetRepository;
import com.simulator.forum.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class AuthenticationController {
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Autowired
	private ResetRepository resetRepository;
	
	@Autowired
	EmailSender sender;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@GetMapping("/login")
	public String login() 
	{
		return "login";
	}
	
	@PostMapping("/valid")
	public ResponseEntity<?> forgotPasswordEmail(@ModelAttribute PasswordUpdateForm data)
	{
		String email = data.getEmail();
		String token = data.getToken();
		String newPassword = data.getNewPassword();

		Reset entity =  resetRepository.findByToken(token);
		
		if(entity ==  null) 
		{
			return new ResponseEntity<>("null" ,HttpStatus.BAD_REQUEST);
		}
		
		if(!validPassword(newPassword)) 
		{
			return new ResponseEntity<>("password"  , HttpStatus.BAD_REQUEST);
		}
		
		if(!entity.getEmail().equals(email)) 
		{
			return new ResponseEntity<>("email no match" , HttpStatus.BAD_REQUEST);
		}
		
		if(!entity.getToken().equals(token)) 
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if(Duration.between( entity.getTime() , ZonedDateTime.now()).toMinutes() < 10) 
		{
			return new ResponseEntity<>("duration" , HttpStatus.BAD_REQUEST);
		}
		
		UserDetail user =  userRepository.findByEmail(email);
		
		String salt = generateSalt();
		
		String hash = hash(newPassword , salt);
		
//		String remoteAddr = request.getRemoteAddr();
		
		user.setPassword(hash);
		user.setSalt(salt);
		
		userRepository.save(user);
		
		return new ResponseEntity<>("ok"  , HttpStatus.OK);
	}
	
	@PostMapping("/token")
	public ResponseEntity<?> forgotPasswordEmail(@RequestBody Map<String , String> emailContainer)
	{
		String email =  emailContainer.get("email");
		
		if(email == null) {
			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if(!validEmail(email)) 
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		if(newEmail(email)) 
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		
		String token = UUID.randomUUID().toString();
		
		Reset entity = new Reset(email , token);
		
		try {
			
			resetRepository.save(entity);
			
		}catch(Exception e) {
			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
		}
		
//		try {
//			
//			sendMail(email , token);
//			
//		}catch(Exception e) {
//			
//			e.printStackTrace();
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//			
//		}
		
		
		return new ResponseEntity<>("ok"  , HttpStatus.OK);
	}
	
	
	private void sendMail(String mail , String token) 
	{
		sender.sendSimpleMessage(mail, "Forgot Password", token);
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(HttpServletRequest request , @ModelAttribute RegisterForm details) 
	{
		String name;
		
		String email;
		
		String password;
		
		try {
			
			name = details.getName();
			
			email = details.getEmail();
			
			password = details.getPassword();
			
			if(email == null || name == null || password == null) {
				
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			
		}catch(Exception e) {
			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
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
		
		
		
		userRepository.createUser(name , email, remoteAddr, remoteAddr, salt, hash);	
		
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
