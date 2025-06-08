package com.simulator.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.transaction.annotation.Transactional;

import com.simulator.forum.entity.UserDetail;

public interface UserRepository extends JpaRepository<UserDetail , Long>
{
	UserDetail findByEmail(String email);
	
	@NativeQuery(value = "select exists (select email from user_detail where email = ?1)")
	Boolean emailExist(String email);
	
	@NativeQuery(value =  "INSERT INTO user_detail (name , email ,  last_login_ip, current_sign_in_ip, salt, password)VALUES (?1, ?2, ?3, ?4 , ?5 , ?6)")
	@Modifying
	@Transactional
	void createUser(String name , String email , String lastLoginIp , String currentSignInIp , String Salt , String Password);
	
	
	
}
