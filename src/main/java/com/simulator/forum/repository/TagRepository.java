package com.simulator.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simulator.forum.entity.Tag;

public interface TagRepository extends JpaRepository<Tag , Long>{
	
	

}
