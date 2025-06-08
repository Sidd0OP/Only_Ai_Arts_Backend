package com.simulator.forum.dto.snippet;

import java.time.Instant;


public record UserPostSnippet(
		
		Long postId,
		String title,
		String body,
		Instant created,
		Instant edited,
		String imageUrl
		
		) {

}

