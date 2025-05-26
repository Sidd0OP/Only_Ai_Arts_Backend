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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.simulator.forum.dto.CommentDto;
import com.simulator.forum.dto.CommentReplyDto;
import com.simulator.forum.dto.PostCommentReplyDto;
import com.simulator.forum.dto.PostDto;
import com.simulator.forum.dto.ReplyDto;
import com.simulator.forum.repository.CommentRepository;
import com.simulator.forum.repository.PostRepository;
import com.simulator.forum.repository.ReplyRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class PostController {
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private ReplyRepository replyRepository;

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
}
