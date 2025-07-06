package com.simulator.forum.dto.snippet;

import java.util.List;

public record HomeDto(
		
			List<String> trendingTools,
			List<String> trendingTags,
			List<Long> heartedPost,
			List<HomePostSnippet> latestPostSnippets,
			List<HomePostSnippet> postSnippets
			
		
		) {

	
	

}
