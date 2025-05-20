package com.simulator.forum.model;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;


public class PostSnippetModel {

	@Column(name = "post_id")
	private long postId;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "body")
	private String body;
	
	@Column(name = "created")
	private ZonedDateTime created;
	
	@Column(name = "edited" , nullable = true)
	private ZonedDateTime edited;
	
	@Column(name = "comment_count")
	private Integer commentCount;
	
	@Column(name = "user_id")
	private long userId;
	
	@Column(name = "name" , nullable = true)
	private String name;
	
	@Column(name = "profile_photo_url" , nullable = true)
	private String userProfilePhotoUrl;
	
	
	
	public PostSnippetModel(
			
			long postId, 
			String title, 
			String body, 
			ZonedDateTime created, 
			ZonedDateTime edited,
			Integer commentCount, 
			long userId, 
			String name, 
			String userProfilePhotoUrl
			
			) {
		
		this.postId = postId;
		this.title = title;
		this.body = body;
		this.created = created;
		this.edited = edited;
		this.commentCount = commentCount;
		this.userId = userId;
		this.name = name;
		this.userProfilePhotoUrl = userProfilePhotoUrl;
	}
	


	

}
