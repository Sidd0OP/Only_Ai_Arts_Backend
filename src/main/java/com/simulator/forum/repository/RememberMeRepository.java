package com.simulator.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simulator.forum.entity.Remember;

import jakarta.transaction.Transactional;

public interface RememberMeRepository extends JpaRepository<Remember , String>{

	 @Transactional
     void deleteByEmail(String email);
}
