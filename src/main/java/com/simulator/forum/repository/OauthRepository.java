package com.simulator.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import com.simulator.forum.entity.Oauth;

public interface OauthRepository extends JpaRepository<Oauth , Long>{

	@NativeQuery(value = "select exists (select email from oauth where email = ?1)")
	Boolean emailExist(String email);
	
	Oauth findByEmail(String email);
	
}
