package com.simulator.forum.dto;

import java.util.List;

public record PostCommentReplyDto
	(
			boolean postEditableByUser,
			List<Long> listOfEditableComments,
			List<Long> listOfEditableReplies,
			
			PostDto post,
			List<CommentReplyDto> discussion
			
			
	) {

}
