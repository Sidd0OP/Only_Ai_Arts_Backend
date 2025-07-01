package com.simulator.forum.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.simulator.forum.dto.PostDto;
import com.simulator.forum.dto.snippet.GallerySnippet;
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
			p.image_url ,
			p.heart,
			p.model,
			p.rated,
			u.id as user_id , 
			u.name ,
			u.profile_photo_url,
			string_agg(t.text, ',') AS tags
			from post p 
			join user_detail u on
			p.user_id = u.id
			LEFT join tag t on 
			p.id = t.post_id
			GROUP BY 
		    p.id, p.title, p.body, p.created, p.edited, p.comment_count, 
		    p.image_url, p.heart, p.model, p.rated,
		    u.id, u.name, u.profile_photo_url
			order by p.heart desc , p.created desc , p.comment_count desc
			limit 20
			offset ?1;
			
			""" , nativeQuery = true)
	List<HomePostSnippet> getPostSnippets(int offset);
	
	@Query(value =  """
			
			select 
			p.id as post_id, 
			p.title , 
			p.image_url
			from post p 
			order by p.heart desc , p.created desc , p.comment_count desc
			limit 40
			offset ?1;
			
			""" , nativeQuery = true)
	List<GallerySnippet> getGallerySnippets(int offset);
	
	
	
	
	@Query(value =  """
			
			select 
			p.id as post_id, 
			p.title , 
			p.body ,
			p.created ,
			p.edited , 
			p.comment_count , 
			p.image_url ,
			p.heart,
			p.model,
			p.rated,
			u.id as user_id , 
			u.name ,
			u.profile_photo_url,
			string_agg(t.text, ',') AS tags
			from post p 
			join user_detail u on
			p.user_id = u.id
			LEFT join tag t on 
			p.id = t.post_id
			GROUP BY 
		    p.id, p.title, p.body, p.created, p.edited, p.comment_count, 
		    p.image_url, p.heart, p.model, p.rated,
		    u.id, u.name, u.profile_photo_url
			order by p.created desc 
			limit 10;
			
			""" , nativeQuery = true)
	List<HomePostSnippet> getLatestPostSnippets();
	
	
	List<Post> findAllByUserId(long userId);
	
	
	
	@Query(value =  """
			
			select 
			p.id as post_id, 
			p.title , 
			p.body ,
			p.created ,
			p.edited , 
			p.comment_count , 
			p.image_url ,
			p.heart,
			p.model,
			p.rated,
			u.id as user_id , 
			u.name ,
			u.profile_photo_url,
			string_agg(t.text, ',') AS tags
			from post p 
			join user_detail u on
			p.user_id = u.id and p.id = ?1
			LEFT join tag t on 
			p.id = t.post_id
			GROUP BY 
		    p.id, p.title, p.body, p.created, p.edited, p.comment_count, 
		    p.image_url, p.heart, p.model, p.rated,
		    u.id, u.name, u.profile_photo_url
			
			""" , nativeQuery = true)
	Optional<PostDto> getPostSnippetFromId(long postId);
	
	
	@Query(value =  """
			
			select add_post(?1 , ?2 , ?3 , ?4 , ?5 , ?6);
			
			""" , nativeQuery = true)
	Long createNewPost(long userId , String title , String body , String imageUrl , String model , Boolean rated);
	
	
	
	@Query(value =  """
			
			select update_post_on_edit(?1)
			
			""" , nativeQuery = true)
	Integer updatePost(long postId);
	
	
	
	
	
	@Query(value =  """
			
			select * from similar_post(?1)
			
			""" , nativeQuery = true)
	List<HomePostSnippet> selectSimilarPost(long postId);
	
	@Query(value =  """
			
			select * from search_post(?1)
			
			""" , nativeQuery = true)
	List<HomePostSnippet> searchPost(String query);
}
