package com.simulator.forum.dto.snippet;

import java.time.Instant;

public record ReplySnippet
		(
				Long replyId,
				Long userId,
				Long commentId,
				String body,
				Instant created,
				Instant edited,
				String userName,
				String userProfliePhotoUrl,
				Boolean editable
				
		) {

}
