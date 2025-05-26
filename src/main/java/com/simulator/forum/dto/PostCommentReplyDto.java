package com.simulator.forum.dto;

import java.util.List;

public record PostCommentReplyDto
	(
			PostDto post,
			List<CommentReplyDto> discussion
			
	) {

}
