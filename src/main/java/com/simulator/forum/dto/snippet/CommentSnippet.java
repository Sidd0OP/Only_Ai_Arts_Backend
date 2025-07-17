package com.simulator.forum.dto.snippet;

import java.time.Instant;

public record CommentSnippet
		(
				Long commentId,
				Long userId,
				String body,
				Instant created,
				Instant edited,
				String userName,
				String userProfliePhotoUrl,
				Boolean editable
		) {

}
