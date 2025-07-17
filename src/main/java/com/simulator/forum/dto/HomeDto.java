package com.simulator.forum.dto;

import java.util.List;

import com.simulator.forum.dto.snippet.PostSnippet;

public record HomeDto(
		
			List<String> trendingTools,
			List<String> trendingTags,
			List<Long> heartedPost,
			List<PostSnippet> latestPostSnippets,
			List<PostSnippet> postSnippets
			
		
		) {

	
	

}
