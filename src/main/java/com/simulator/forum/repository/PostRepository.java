package com.simulator.forum.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.simulator.forum.dto.snippet.GallerySnippet;
import com.simulator.forum.dto.snippet.PostSnippet;
import com.simulator.forum.entity.Post;



public interface PostRepository extends JpaRepository<Post , Long>{
	
	List<Post> findAllByUserId(long userId);
	
	
	@Query(value =  """
			
			select 
			DISTINCT model 
			from (
				select  model , heart , comment_count from post 
				where model is not null AND TRIM(model) <> '' 
				order by heart desc ,
				comment_count desc
				limit 100
			) limit 5;

			
			""" , nativeQuery = true)
	List<String> getTrendingTool();
	
	
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
			
			select * from get_post_page(?1 , ?2);
			
			""" , nativeQuery = true)
	List<PostSnippet> getPostSnippets(int offset , long userId);
	
	
	
	
	
	
	@Query(value =  """
			
			select * from get_post_latest(?1 , ?1);
			
			""" , nativeQuery = true)
	List<PostSnippet> getLatestPostSnippets(int offset , long userId);
	
	
	
	@Query(value =  """
			
			select * from get_post_of_tag(?1 , ?2 , ?3);
			
			""" , nativeQuery = true)
	List<PostSnippet> getPostSnippetsFromTag(Integer offset , String tag , long userId);
	
	
	
	@Query(value =  """
			
			select * from get_post_of_id(?1 , ?2);
			
			""" , nativeQuery = true)
	Optional<PostSnippet> getPostSnippetFromId(long postId , long userId);
	

	
	@Query(value =  """
			
			select add_post(?1 , ?2 , ?3 , ?4 , ?5 , ?6);
			
			""" , nativeQuery = true)
	Long createNewPost(long userId , String title , String body , String imageUrl , String model , Boolean rated);
	
	
	
	@Query(value =  """
			
			select update_post_on_edit(?1)
			
			""" , nativeQuery = true)
	Integer updatePost(long postId);
	
	
	
	@Query(value =  """
			
			select * from get_post_of_user(?1 , ?2 , ?3)
			
			""" , nativeQuery = true)
	List<PostSnippet> postOfUser(int offset , long userId , long loggedInUserId);
	
	@Query(value =  """
			
			select * from search_post(?1)
			
			""" , nativeQuery = true)
	List<PostSnippet> searchPost(String query);
}
