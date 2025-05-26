package com.simulator.forum.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;

import com.simulator.forum.entity.Remember;
import com.simulator.forum.repository.RememberMeRepository;

@Service
public class RememberMeService implements PersistentTokenRepository 
{
	@Autowired
	private RememberMeRepository repository;
	
	
	

	@Override
	public void createNewToken(PersistentRememberMeToken token) 
	{
		
		System.out.println(token.getSeries() + " " + token.getTokenValue() + " " + token.getUsername());
		
		Remember entity = new Remember();
		entity.setSeries(token.getSeries());
		entity.setToken(token.getTokenValue());
		entity.setDevice("test");
		entity.setEmail(token.getUsername());
		
		repository.save(entity);
	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		
		
		Remember entity = repository.findById(series).get();
		entity.setToken(tokenValue);
		repository.save(entity);

	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		
		
		
		return repository.findById(seriesId)
                .map(token -> new PersistentRememberMeToken(
                        token.getEmail(),
                        token.getSeries(),
                        token.getToken(),
                        Date.from(token.getLastUsed().toInstant()))
                )
                .orElse(null);
		
		
	}

	@Override
	public void removeUserTokens(String username) {
		
		System.out.println("REMOVE TOKEN CALLED");
		
	}
	
}
