package com.simulator.forum.model;

public class CommentForm {
	
	private Long PostId;
	private String body ;

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
