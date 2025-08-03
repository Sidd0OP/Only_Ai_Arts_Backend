package com.simulator.forum.dto;

import java.util.List;

import com.simulator.forum.dto.snippet.PostSnippet;
import com.simulator.forum.dto.snippet.UserProfileSnippet;

public record PostPageDto
	(
			PostSnippet post,
			UserProfileSnippet profile,
			List<CommentReplyDto> discussion,
			List<PostSnippet> similarPosts
			
			
	) {

}
