package com.simulator.forum.dto;

import java.util.List;

public record CommentReplyDto
		(
				CommentDto comment,
				List<ReplyDto> replies
		) {

}
