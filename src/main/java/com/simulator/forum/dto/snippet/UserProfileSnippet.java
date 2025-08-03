package com.simulator.forum.dto.snippet;

import java.time.Instant;

public record UserProfileSnippet(
		
		boolean editable,
		Long userId,
		String name, 
		String profilePhotoUrl,
		Instant joined,
		String bio
		
		) {

}
