package com.simulator.forum.dto;

import java.time.Instant;


public record PostSnippet(
		
		Long postId,
		String title,
		String body,
		Instant created,
		Instant edited,
		Integer commentCount,
		Long userId,
		String userName,
		String userProfliePhotoUrl
		
		) {

}

