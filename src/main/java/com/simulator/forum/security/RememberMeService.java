package com.simulator.forum.security;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.log.LogMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



@Service
public class RememberMeService  extends AbstractRememberMeServices{
	

	
	private SecureRandom random;
	
	private PersistentRememberMeToken token;

	public static final int DEFAULT_SERIES_LENGTH = 16;

	public static final int DEFAULT_TOKEN_LENGTH = 16;

	private int seriesLength = DEFAULT_SERIES_LENGTH;

	private int tokenLength = DEFAULT_TOKEN_LENGTH;

	@Autowired
	public RememberMeService(DatabaseUserDetailService databaseUserDetailService) {
		super("key", databaseUserDetailService);
		
		this.random = new SecureRandom();
		
	}
	
	
	public PersistentRememberMeToken createCookie(HttpServletRequest request, HttpServletResponse response,
			Authentication successfulAuthentication) 
	{
		
		onLoginSuccess(request , response , successfulAuthentication);
		
		return this.token;
	}

	@Override
	protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication successfulAuthentication) {
		
		String username = successfulAuthentication.getName();
		this.logger.debug(LogMessage.format("Creating new persistent login for user %s", username));
		PersistentRememberMeToken persistentToken = new PersistentRememberMeToken(username, generateSeriesData(),
				generateTokenData(), new Date());
		
		try {
			System.out.println("SAVE COOKIE" + persistentToken.getSeries());
//			this.rememberMeRepositoryService.createNewToken(persistentToken);
			addCookie(persistentToken, request, response);
		}
		catch (Exception ex) {
			this.logger.error("Failed to save persistent token ", ex);
		}
		
		this.token = persistentToken;
	}

	@Override
	protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request,
			HttpServletResponse response) throws RememberMeAuthenticationException, UsernameNotFoundException {
		
		System.out.println("Auto Login Called");
		
		return null;
	}
	
	private void addCookie(PersistentRememberMeToken token, HttpServletRequest request, HttpServletResponse response) {
		setCookie(new String[] { token.getSeries(), token.getTokenValue() }, getTokenValiditySeconds(), request,
				response);
	}

	protected String generateSeriesData() {
		byte[] newSeries = new byte[this.seriesLength];
		this.random.nextBytes(newSeries);
		return new String(Base64.getEncoder().encode(newSeries));
	}

	protected String generateTokenData() {
		byte[] newToken = new byte[this.tokenLength];
		this.random.nextBytes(newToken);
		return new String(Base64.getEncoder().encode(newToken));
	}


}
