package com.simulator.forum.dto.snippet;

import java.util.List;

public record HomeDto(
			
			Long userId,
			List<HomePostSnippet> postSnippets
			
		
		) {

}
