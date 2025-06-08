package com.simulator.forum.google;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

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
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;


@RestController
public class Auth {
	
	final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	
	@Value("${oauth2.client.registration.google.client-id}")
	private String clientId;
	
	@Value("${oauth2.client.registration.google.client-secret}")
    private String secretKey;
	
	List<String> scopes = List.of(
		    "https://www.googleapis.com/auth/userinfo.email",
		    "https://www.googleapis.com/auth/userinfo.profile"
		);
	
	@PostMapping("/auth/google")
	public ResponseEntity<?> getToken(@RequestBody Map<String, String> body){
		
		String code = body.get("code");
		
		GoogleIdToken idToken;
		GoogleTokenResponse tokenResponse;

		
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
		
		String email = idToken.getPayload().getEmail();
		
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
