package com.simulator.forum.dto;

import java.time.Instant;

public record CommentDto
		(
				Long commentId,
				Long userId,
				String body,
				Instant created,
				Instant edited,
				String userName,
				String userProfliePhotoUrl
		) {

}
