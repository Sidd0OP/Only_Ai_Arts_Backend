package com.simulator.forum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.simulator.forum.dto.CommentDto;
import com.simulator.forum.dto.snippet.CommentSnippet;
import com.simulator.forum.dto.snippet.HomePostSnippet;
import com.simulator.forum.entity.Comment;


public interface CommentRepository extends JpaRepository<Comment , Long>{
	
	
	@Query(value =  """
			
			select
			c.id as comment_id,
			c.user_id , 
			c.body , 
			c.created , 
			c.edited ,
			u.name as user_name, 
			u.profile_photo_url 
			from comment c join user_detail u on 
			c.user_id = u.id and c.post_id = ?1;
			
			""" , nativeQuery = true)
	List<CommentDto> getAllComments(long postId);

	List<Comment> findAllByUserId(long userId);
}
