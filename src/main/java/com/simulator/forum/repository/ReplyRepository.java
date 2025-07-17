package com.simulator.forum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.simulator.forum.dto.snippet.ReplySnippet;
import com.simulator.forum.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply , Long>{
	
	
	@Query(value =  """
			
			select 
			r.id as reply_id,
			r.user_id , 
			r.comment_id ,
			r.body , 
			r.created , 
			r.edited ,
			u.name as user_name, 
			u.profile_photo_url,
			exists ( select 1 from reply r1 where r1.user_id = ?2) as editable
			from reply r join user_detail u 
			on r.user_id = u.id and r.post_id = ?1
			
			""" , nativeQuery = true)
	List<ReplySnippet> getAllReplies(long postId , long userId);

	
	List<Reply> findAllByUserId(long userId);
	
	
	@Query(value =  """
			
			select add_reply(?1 , ?2 , ?3 , ?4);
			
			""" , nativeQuery = true)
	@Modifying
	@Transactional
	Object[] createNewReply(long commentId , long userId , long postId ,  String body);
	
	
	@Query(value =  """
			
			select update_reply_on_edit(?1)
			
			""" , nativeQuery = true)
	Integer updateReply(long replyId);
}
