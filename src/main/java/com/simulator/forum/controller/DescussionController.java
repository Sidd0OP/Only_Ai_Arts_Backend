package com.simulator.forum.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.simulator.forum.dto.CommentDto;
import com.simulator.forum.dto.CommentReplyDto;
import com.simulator.forum.dto.PostCommentReplyDto;
import com.simulator.forum.dto.PostDto;
import com.simulator.forum.dto.ReplyDto;
import com.simulator.forum.entity.UserDetail;
import com.simulator.forum.model.CommentForm;
import com.simulator.forum.model.PostForm;
import com.simulator.forum.model.ReplyForm;
import com.simulator.forum.repository.CommentRepository;
import com.simulator.forum.repository.PostRepository;
import com.simulator.forum.repository.ReplyRepository;
import com.simulator.forum.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class DescussionController {
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private ReplyRepository replyRepository;
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/post/{id}")
	public ResponseEntity<?> post(HttpServletRequest request , @PathVariable String id) 
	{
		
		
		if(id == null) 
		{
			return new ResponseEntity<>("format = post/id"  , HttpStatus.BAD_REQUEST);
		}
		
		long postId;
		
		try 
		{
			postId = Long.valueOf(id);
			
		}catch(NumberFormatException e) {
			
			return new ResponseEntity<>("numeric parameter required"  , HttpStatus.BAD_REQUEST);
		}
		
		
		Optional<PostDto> post = postRepository.getPostSnippetFromId(postId);
		
		
		if(!post.isPresent()) 
		{
			return new ResponseEntity<>("Not Found"  , HttpStatus.NOT_FOUND);
		}
		
		List<CommentDto> comments = commentRepository.getAllComments(postId);
		List<ReplyDto> replies = replyRepository.getAllReplies(postId);
		

		
		List<CommentReplyDto> commentReplies = comments.stream()
				.map(c -> 
						new CommentReplyDto
						(c , replies.stream().
							 filter(r -> r.commentId().equals(c.commentId()))
							 .toList())).toList();
				

		return new ResponseEntity<>(new PostCommentReplyDto(post.get() , commentReplies) , HttpStatus.OK);
		
	}
	
	
	private UserDetail findUserFromSession() 
	{
		Authentication  authObject  = SecurityContextHolder.getContext().getAuthentication();
			
		
		if(authObject != null && authObject.isAuthenticated()) 
		{
			UserDetail authenticatedUser = userRepository.findByEmail(authObject.getName());
			
			return authenticatedUser;
			
		}else {return null;}
	}
	
	@PostMapping("/new")
	public ResponseEntity<?> createPost(@ModelAttribute PostForm postDetails)
	{
		
		UserDetail user =  findUserFromSession();	

		if(user ==  null) {return new ResponseEntity<>("Login to create Post"  , HttpStatus.BAD_REQUEST);}
		
		try 
		{
			postRepository.createNewPost(
					user.getId() , 
					postDetails.getTitle() , 
					postDetails.getBody() , 
					null);
			
			return new ResponseEntity<>("Insert Sucessful"  , HttpStatus.OK);
			
		}catch(Exception e) 
		{
			return new ResponseEntity<>("Failed"  , HttpStatus.BAD_REQUEST);
		}

	}
	
	
	@PostMapping("/comment")
	public ResponseEntity<?> createComment(@ModelAttribute CommentForm commentDetails)
	{
		
		UserDetail user =  findUserFromSession();	

		if(user ==  null) {return new ResponseEntity<>("Login To Post Comment"  , HttpStatus.BAD_REQUEST);}
		
		try 
		{
			commentRepository.createNewComment(
					commentDetails.getPostId() ,
					user.getId() , 
					commentDetails.getBody()
					);
			
			return new ResponseEntity<>("Insert Sucessful"  , HttpStatus.OK);
			
		}catch(Exception e) 
		{
			return new ResponseEntity<>("Failed"  , HttpStatus.BAD_REQUEST);
		}

	}
	
	
	@PostMapping("/reply")
	public ResponseEntity<?> createReply(@ModelAttribute ReplyForm replyDetails)
	{
		
		UserDetail user =  findUserFromSession();	

		if(user ==  null) {return new ResponseEntity<>("Login To Post Reply"  , HttpStatus.BAD_REQUEST);}
		
		try 
		{
			replyRepository.createNewReply(
					replyDetails.getCommentId(),
					user.getId() , 
					replyDetails.getPostId() ,
					replyDetails.getBody()
					);
			
			return new ResponseEntity<>("Insert Sucessful"  , HttpStatus.OK);
			
		}catch(Exception e) 
		{
			return new ResponseEntity<>("Failed"  , HttpStatus.BAD_REQUEST);
		}

	}
}
