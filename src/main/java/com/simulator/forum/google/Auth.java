package com.simulator.forum.google;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
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
import com.simulator.forum.entity.UserDetail;
import com.simulator.forum.repository.OauthRepository;
import com.simulator.forum.repository.UserRepository;
import com.simulator.forum.security.DatabaseUserDetailService;
import com.simulator.forum.security.CustomTokenRepository;
import com.simulator.forum.security.RememberMeService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@RestController
public class Auth {
	
	final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	
	@Autowired
	private RememberMeService rememberMeService;
	
	@Autowired
	private DatabaseUserDetailService databaseUserDetailService;
	
	@Autowired 
	private CustomTokenRepository tokenRepository;
	
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
	public ResponseEntity<?> getToken(HttpServletRequest request , HttpServletResponse response , @RequestBody Map<String, String> body){
		
		String code = body.get("code");
		
		GoogleIdToken idToken;
		GoogleTokenResponse tokenResponse;
		
		String email;
		String subject;
		String autoPassword;
		
		
		
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

		autoPassword = UUID.randomUUID().toString();
		String salt = generateSalt();
		String hash = hash(autoPassword , salt);
		  
		email = payload.getEmail();
		subject = autoPassword;
		
		
		
		
		if(userRepository.emailExist(email)) 
		{
			UserDetail entity = userRepository.findByEmail(email);
			
			entity.setPassword(hash);
			entity.setSalt(salt);
			entity.setCurrentSignInIp(request.getRemoteAddr());
			
			userRepository.save(entity);
			
		}else {
			
			userRepository.createUser("" , email, 
					request.getRemoteAddr(), 
					request.getRemoteAddr(), 
					salt ,
					hash);
		}
		
		
		if(!oauthRepository.emailExist(email)) 
		{
				
			oauthRepository.save(new Oauth(email , subject));
				
		}
		
	
		return new ResponseEntity<>(Map.of("email" , email , "token" , autoPassword)  , HttpStatus.OK);
		
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
	
//	protected String generateSeriesData() {
//		byte[] newSeries = new byte[this.seriesLength];
//		this.random.nextBytes(newSeries);
//		return new String(Base64.getEncoder().encode(newSeries));
//	}
//
//	protected String generateTokenData() {
//		byte[] newToken = new byte[this.tokenLength];
//		this.random.nextBytes(newToken);
//		return new String(Base64.getEncoder().encode(newToken));
//	}
	
	
}
