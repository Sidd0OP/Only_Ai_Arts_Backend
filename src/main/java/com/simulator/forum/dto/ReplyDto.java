package com.simulator.forum.dto;

import java.time.Instant;

public record ReplyDto
		(
				Long replyId,
				Long userId,
				Long commentId,
				String body,
				Instant created,
				Instant edited,
				String userName,
				String userProfliePhotoUrl
				
		) {

}
