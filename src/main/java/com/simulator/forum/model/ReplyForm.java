package com.simulator.forum.model;

public class ReplyForm {

	private Long CommentId;
	private Long PostId;
	private String body;
	
	public Long getCommentId() {
		return CommentId;
	}
	public void setCommentId(Long commentId) {
		CommentId = commentId;
	}
	public Long getPostId() {
		return PostId;
	}
	public void setPostId(Long postId) {
		PostId = postId;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
}
