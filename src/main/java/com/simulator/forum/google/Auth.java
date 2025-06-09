package com.simulator.forum.google;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.simulator.forum.entity.Oauth;
import com.simulator.forum.repository.OauthRepository;
import com.simulator.forum.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;


@RestController
public class Auth {
	
	final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OauthRepository oauthRepository;
	
	@Value("${oauth2.client.registration.google.client-id}")
	private String clientId;
	
	@Value("${oauth2.client.registration.google.client-secret}")
    private String secretKey;
	
	List<String> scopes = List.of(
		    "https://www.googleapis.com/auth/userinfo.email",
			"https://www.googleapis.com/auth/userinfo.profile",
			"https://www.googleapis.com/auth/openid"
		);
	
	@PostMapping("/auth/google")
	public ResponseEntity<?> getToken(HttpServletRequest request , @RequestBody Map<String, String> body){
		
		String code = body.get("code");
		
		GoogleIdToken idToken;
		GoogleTokenResponse tokenResponse;
		
		String email;
		String subject;
		
		
		
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport() , JSON_FACTORY)
			    .setAudience(Collections.singletonList(clientId))
			    .build();

		
		try {
				
				tokenResponse = getResponse(code);
				
		} catch (IOException | GeneralSecurityException e) {
				
			return new ResponseEntity<>("failed"  , HttpStatus.OK);
		}
		
		
		
		
		try {
			
			
			idToken = tokenResponse.parseIdToken();
			
		} catch (IOException e) {
			
			return new ResponseEntity<>("failed"  , HttpStatus.OK);
			
		}
		
		
		try {
			
			verifier.verify(idToken);
			
		} catch (GeneralSecurityException | IOException e) {
			
			return new ResponseEntity<>("failed"  , HttpStatus.OK);
		}
		
		
		Payload payload = idToken.getPayload();

		  
		email = payload.getEmail();
		subject = payload.getSubject();
		
		
		if(userRepository.emailExist(email))
		{
			if(!oauthRepository.emailExist(email)) 
			{
				
				oauthRepository.save(new Oauth(email , subject));
				//send cookie
				
			}
		}
		
		
		if(!userRepository.emailExist(email))
		{
			
			userRepository.createUser("Bob" , email, request.getRemoteAddr(), 
					request.getRemoteAddr(), "" , UUID.randomUUID().toString());
			
			if(!oauthRepository.emailExist(email)) 
			{
				oauthRepository.save(new Oauth(email , subject));
				//send cookie
					
			}
		}
		
		
		
		
		
		return new ResponseEntity<>("ok"  , HttpStatus.OK);
		
	}
	
	
	private GoogleTokenResponse getResponse(String code) throws IOException, GeneralSecurityException 
	{
				
		GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(
		         new NetHttpTransport(), 
		         JSON_FACTORY,
		         clientId, 
		         secretKey,
		         code , 
				 "postmessage")
		         .execute();
		
		return response;
	}
}
