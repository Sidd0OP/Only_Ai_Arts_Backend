package com.simulator.forum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.simulator.forum.entity.Tag;

public interface TagRepository extends JpaRepository<Tag , Long>{
	
	
	
	@Query(value =  """
			
			SELECT t.text as top_tags
			FROM tag t
			JOIN post p ON p.id = t.post_id
			ORDER BY p.heart DESC
			LIMIT 50;

			
			""" , nativeQuery = true)
	List<String> getTrendingTags();
	

}
