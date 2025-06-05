package com.simulator.forum.controller;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.simulator.forum.cloudflare.MediaService;
import com.simulator.forum.dto.CommentDto;
import com.simulator.forum.dto.CommentReplyDto;
import com.simulator.forum.dto.PostCommentReplyDto;
import com.simulator.forum.dto.PostDto;
import com.simulator.forum.dto.ReplyDto;
import com.simulator.forum.dto.snippet.HomePostSnippet;
import com.simulator.forum.entity.Comment;
import com.simulator.forum.entity.Post;
import com.simulator.forum.entity.Reply;
import com.simulator.forum.entity.UserDetail;
import com.simulator.forum.model.CommentForm;
import com.simulator.forum.model.PostForm;
import com.simulator.forum.model.ReplyForm;
import com.simulator.forum.model.update.CommentReplyUpdateForm;
import com.simulator.forum.model.update.PostUpdateForm;
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
	
	@Autowired
	MediaService mediaSerive;
	
	
	private UserDetail findUserFromSession() 
	{
		Authentication  authObject  = SecurityContextHolder.getContext().getAuthentication();
			
		
		if(authObject != null && authObject.isAuthenticated()) 
		{
			UserDetail authenticatedUser = userRepository.findByEmail(authObject.getName());
			
			return authenticatedUser;
			
		}else {return null;}
	}
	
	
	

	@GetMapping("/post/{postId}")
	public ResponseEntity<?> post(HttpServletRequest request , @PathVariable Long postId ) 
	{
		
		UserDetail user =  findUserFromSession();	
		
		boolean postEditable = false;
				
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
		
		
		List<HomePostSnippet> similarPost;
		
		try {
			similarPost = postRepository.selectSimilarPost(postId);
			
		}catch(Exception e) {
			similarPost = List.of();
		}
		
		
		
		if(user != null) 
		{
			long userId = user.getId();
			
			if(post.get().userId() == userId) 
			{

				postEditable = true;
				
			}
			
			PostCommentReplyDto postCommentReplyDto = new PostCommentReplyDto
					(
							postEditable,
							
							comments.stream()
							.filter( c -> c.userId() == userId)
							.map(CommentDto::commentId).toList(),
							
							replies.stream()
							.filter(r -> r.userId() == userId)
							.map(ReplyDto::replyId)
							.toList(),
							
							post.get(),
							
							commentReplies,
							
							similarPost
							
					);
			
			return new ResponseEntity<>(postCommentReplyDto , HttpStatus.OK);
			
		}
				

		return new ResponseEntity<>(new PostCommentReplyDto(false ,
														List.of() , 
														List.of() , 
														post.get() , 
														commentReplies,
														similarPost) , HttpStatus.OK);
		
	}
	
	
	
	
	
	
	
	@PostMapping("/new")
	public ResponseEntity<?> createPost(@ModelAttribute PostForm postDetails , 
										@RequestParam(value = "file" , required = false) MultipartFile file)
	{
		
		UserDetail user =  findUserFromSession();	
		String url = "";
		
		if(user ==  null) {return new ResponseEntity<>("Login to create Post"  , HttpStatus.BAD_REQUEST);}
		
		try 
		{
			if(file != null) 
			{
				url = mediaSerive.uploadFile(file , "image");
			}
			
						
		}catch(Exception e) {
			return new ResponseEntity<>("Failed"  , HttpStatus.BAD_REQUEST);
		}
		
		
		
		try 
		{
			postRepository.createNewPost(
					user.getId() , 
					postDetails.getTitle() , 
					postDetails.getBody() , 
					url);
			
			return new ResponseEntity<>("Insert Sucessful"  , HttpStatus.OK);
			
		}catch(Exception e) 
		{
			return new ResponseEntity<>("Failed"  , HttpStatus.BAD_REQUEST);
		}

	}
	
	
	@PatchMapping("/new")
	public ResponseEntity<?> updatePost(@ModelAttribute PostUpdateForm postUpdateDetails)
	{
		UserDetail user =  findUserFromSession();	

		if(user ==  null) {return new ResponseEntity<>("Login to update"  , HttpStatus.BAD_REQUEST);}
		
		Optional<Post> post = postRepository.findById(postUpdateDetails.getPostId());
		
		if(post.isEmpty()) {return new ResponseEntity<>("Post Not Found"  , HttpStatus.NOT_FOUND);}
		
		if(post.get().getUserId() != user.getId()) {return new ResponseEntity<>("Not allowed"  , HttpStatus.FORBIDDEN);}
		
		
		Post entity = post.get();
		
		entity.setTitle(postUpdateDetails.getTitle());
		entity.setBody(postUpdateDetails.getBody());
		
		try 
		{
			postRepository.save(entity);
			postRepository.updatePost(entity.getId());
			return new ResponseEntity<>("Update Sucessful"  , HttpStatus.OK);
			
		}catch(Exception e) 
		{
			return new ResponseEntity<>("Update Failed"  , HttpStatus.BAD_REQUEST);
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
	
	
	@PatchMapping("/comment")
	public ResponseEntity<?> updateComment(@ModelAttribute CommentReplyUpdateForm commentReplyUpdateForm)
	{
		UserDetail user =  findUserFromSession();	

		if(user ==  null) {return new ResponseEntity<>("Login to update"  , HttpStatus.BAD_REQUEST);}
		
		Optional<Comment> comment = commentRepository.findById(commentReplyUpdateForm.getId());
		
		if(comment.isEmpty()) {return new ResponseEntity<>("Comment Not Found"  , HttpStatus.NOT_FOUND);}
		
		if(comment.get().getUserId() != user.getId()) {return new ResponseEntity<>("Not allowed"  , HttpStatus.FORBIDDEN);}
		
		
		Comment entity = comment.get();
		
		
		entity.setBody(commentReplyUpdateForm.getBody());
		
		
		try 
		{
			commentRepository.save(entity);
			//this was supposed to be done by PG trigger but it not working cuz after update of row pg wont overwrite that idk...
			//ik this is stupid 
			commentRepository.updateComment(entity.getId());
			
			return new ResponseEntity<>("Update Sucessful"  , HttpStatus.OK);
			
		}catch(Exception e) 
		{
			return new ResponseEntity<>("Update Failed"  , HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
	
	
	
	@PostMapping("/reply")
	public ResponseEntity<?> createReply(@ModelAttribute ReplyForm replyDetails)
	{
		
		UserDetail user =  findUserFromSession();	

		if(user ==  null) {return new ResponseEntity<>("Login To Post Reply"  , HttpStatus.BAD_REQUEST);}
		
		Optional<Comment> optionalComment = commentRepository.findById(replyDetails.getCommentId());

		if (optionalComment.isPresent()) {
		    Comment c = optionalComment.get();
		    if (c.getPostId() != replyDetails.getPostId()) {
		    	
		        return new ResponseEntity<>("U tried to act smart", HttpStatus.BAD_REQUEST);
		    }
		    
		} else {
			
		    return new ResponseEntity<>("Comment not found", HttpStatus.BAD_REQUEST);
		}
		
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
	
	
	@PatchMapping("/reply")
	public ResponseEntity<?> updateReply(@ModelAttribute CommentReplyUpdateForm commentReplyUpdateForm)
	{
		UserDetail user =  findUserFromSession();	

		if(user ==  null) {return new ResponseEntity<>("Login to update"  , HttpStatus.BAD_REQUEST);}
		
		Optional<Reply> reply = replyRepository.findById(commentReplyUpdateForm.getId());
		
		if(reply.isEmpty()) {return new ResponseEntity<>("Comment Not Found"  , HttpStatus.NOT_FOUND);}
		
		if(reply.get().getUserId() != user.getId()) {return new ResponseEntity<>("Not allowed"  , HttpStatus.FORBIDDEN);}
		
		
		Reply entity = reply.get();
		
		
		entity.setBody(commentReplyUpdateForm.getBody());
		
		
		try 
		{
			replyRepository.save(entity);
			replyRepository.updateReply(entity.getId());
			return new ResponseEntity<>("Update Sucessful"  , HttpStatus.OK);
			
		}catch(Exception e) 
		{
			return new ResponseEntity<>("Update Failed"  , HttpStatus.BAD_REQUEST);
		}
	}
}
