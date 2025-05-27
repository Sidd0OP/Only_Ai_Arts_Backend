package com.simulator.forum.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.simulator.forum.dto.PostDto;
import com.simulator.forum.dto.snippet.HomePostSnippet;
import com.simulator.forum.entity.Post;



public interface PostRepository extends JpaRepository<Post , Long>{

	
	@Query(value =  """
			
			select 
			p.id as post_id, 
			p.title , 
			p.body ,
			p.created ,
			p.edited , 
			p.comment_count , 
			u.id as user_id , 
			u.name ,
			u.profile_photo_url 
			from post p join user_detail u on
			p.user_id = u.id
			order by p.created asc 
			limit 20
			offset ?1
			
			""" , nativeQuery = true)
	List<HomePostSnippet> getPostSnippets(int offset);
	
	
	
	List<Post> findAllByUserId(long userId);
	
	
	
	
	@Query(value =  """
			
			select p.id as post_id , 
			p.user_id , 
			p.title , 
			p.body , 
			p.created , 
			p.edited , 
			p.comment_count , 
			p.image_url ,
			u.name , 
			u.profile_photo_url 
			from post p join user_detail u on p.user_id = u.id and p.id = ?1
			
			""" , nativeQuery = true)
	Optional<PostDto> getPostSnippetFromId(long postId);
	
}
