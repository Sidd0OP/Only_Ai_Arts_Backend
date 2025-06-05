package com.simulator.forum.dto;

import java.util.List;

import com.simulator.forum.dto.snippet.HomePostSnippet;

public record PostCommentReplyDto
	(
			boolean postEditableByUser,
			List<Long> listOfEditableComments,
			List<Long> listOfEditableReplies,
			
			PostDto post,
			List<CommentReplyDto> discussion,
			List<HomePostSnippet> similarPosts
			
			
	) {

}
