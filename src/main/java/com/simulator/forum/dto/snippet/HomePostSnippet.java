package com.simulator.forum.dto.snippet;

import java.time.Instant;


public record HomePostSnippet(
		
		Long postId,
		String title,
		String body,
		Instant created,
		Instant edited,
		Integer commentCount,
		Long userId,
		String userName,
		String profliePhotoUrl
		
		) {

}

