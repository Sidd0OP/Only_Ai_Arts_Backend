package com.simulator.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simulator.forum.entity.Reset;
import com.simulator.forum.entity.UserDetail;

import jakarta.transaction.Transactional;

public interface ResetRepository extends JpaRepository<Reset , Long>{

	Reset findByToken(String token);
	
	Reset findByEmail(String email);
	
	@Transactional
    void deleteAllByEmail(String email);
}
