package com.simulator.forum.dto;

import java.util.List;

import com.simulator.forum.dto.snippet.CommentSnippet;
import com.simulator.forum.dto.snippet.ReplySnippet;

public record CommentReplyDto
		(
				CommentSnippet comment,
				List<ReplySnippet> replies
		) {

}
