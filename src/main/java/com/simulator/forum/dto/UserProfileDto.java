package com.simulator.forum.dto;

import java.time.Instant;
import java.util.List;

import com.simulator.forum.dto.snippet.CommentSnippet;
import com.simulator.forum.dto.snippet.ReplySnippet;
import com.simulator.forum.dto.snippet.UserPostSnippet;

public record UserProfileDto(
		
		boolean editable,
		Long userId,
		String name, 
		String profilePhotoUrl,
		Instant joined,
		String bio,
		List<UserPostSnippet> posts,
		List<CommentSnippet> comments ,
		List<ReplySnippet> replies
		
		
		) {

}


