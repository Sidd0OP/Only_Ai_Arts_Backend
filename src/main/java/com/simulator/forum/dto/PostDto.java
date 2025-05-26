package com.simulator.forum.dto;

import java.time.Instant;
import java.util.List;

public record PostDto
	(
		Long postId,
		Long userId,
		String title,
		String body,
		Instant created,
		Instant edited,
		Integer commentCount,
		String imageUrl,
		String userName,
		String userProfliePhotoUrl
	
		
		) {

}
