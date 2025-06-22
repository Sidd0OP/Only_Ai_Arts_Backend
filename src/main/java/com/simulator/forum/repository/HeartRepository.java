package com.simulator.forum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.transaction.annotation.Transactional;

import com.simulator.forum.entity.Heart;


public interface HeartRepository extends JpaRepository<Heart , Long>{

	@NativeQuery(value = "select exists (select 1 from heart where post_id = ?1 and user_id = ?2)")
	Boolean hasUserHearted(long postId , long userId);
	
	
	@NativeQuery(value =  "select heart_post(?1 , ?2)")
	void addHeart(long postId , long userId);
	
	@NativeQuery(value = "select post_id FROM heart where user_id = ?1")
	List<Long> getHeartedPost(long userId);
}
