package com.simulator.forum.dto.snippet;

import java.time.Instant;

public record ReplySnippet(
		
		Long replyId,
		String body,
		Instant created,
		Instant edited
		
		
		) {

}
