package com.simulator.forum.dto.snippet;

import java.time.Instant;

public record CommentSnippet(
		
		Long postId,
		String body,
		Instant created,
		Instant edited,
		Integer replyCount
				
		) {

}
