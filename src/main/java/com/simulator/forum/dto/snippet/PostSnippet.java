package com.simulator.forum.dto.snippet;

import java.time.Instant;


public record PostSnippet(
		
		Long postId,
		String title,
		String body,
		Instant created,
		Instant edited,
		Integer commentCount,
		String imageUrl,
		Integer heart,
		String model,
		Boolean rated,
		Long userId,
		String userName,
		String profliePhotoUrl,
		String tags,
		Boolean hearted
		
		) {

}

