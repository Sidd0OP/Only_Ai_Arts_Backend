package com.simulator.forum.dto;

import java.time.Instant;


public record PostDto
	(
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
		String userProfliePhotoUrl,
		String tags
	
		
		) {

}
