package com.simulator.forum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.simulator.forum.dto.PostSnippet;
import com.simulator.forum.entity.Post;



public interface PostRepository extends JpaRepository<Post , Long>{

	
	@Query(value =  """
			
			select p.id as post_id , p.title , p.body , p.created , p.edited , p.comment_count , u.id as user_id , u.name , u.profile_photo_url
			from post p join user_detail u on
			p.user_id = u.id
			
			""" , nativeQuery = true)
	List<PostSnippet> getPostSnippets();
	
}
