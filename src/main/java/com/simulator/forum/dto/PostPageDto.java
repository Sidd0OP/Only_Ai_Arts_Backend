package com.simulator.forum.dto;

import java.util.List;

import com.simulator.forum.dto.snippet.PostSnippet;

public record PostPageDto
	(
			PostSnippet post,
			List<CommentReplyDto> discussion,
			List<PostSnippet> similarPosts
			
			
	) {

}
