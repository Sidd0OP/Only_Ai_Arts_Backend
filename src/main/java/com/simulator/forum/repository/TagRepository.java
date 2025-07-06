package com.simulator.forum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.simulator.forum.entity.Tag;

public interface TagRepository extends JpaRepository<Tag , Long>{
	
	
	
	@Query(value =  """
			
			SELECT DISTINCT text
			FROM (
			  SELECT t.text, p.heart
			  FROM tag t
			  JOIN post p ON p.id = t.post_id
			  ORDER BY p.heart DESC
			  LIMIT 100
			) AS top_tags
			LIMIT 10;

			
			""" , nativeQuery = true)
	List<String> getTrendingTags();
	

}
