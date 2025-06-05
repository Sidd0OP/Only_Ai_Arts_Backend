package com.simulator.forum.dto.snippet;

import java.util.List;

public record HomeDto(
			
			List<HomePostSnippet> latestPostSnippets,
			List<HomePostSnippet> postSnippets
			
		
		) {

}
