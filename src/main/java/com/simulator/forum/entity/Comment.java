package com.simulator.forum.entity;

import java.time.ZonedDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "comment")
@Getter
@Setter
public class Comment {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	private long postId;
	
	private long userId;
	
	private String body;
	
	private ZonedDateTime created;
	
	private ZonedDateTime edited;
	
	private Integer editCount;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPostId() {
		return postId;
	}

	public void setPostId(long postId) {
		this.postId = postId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public ZonedDateTime getCreated() {
		return created;
	}

	public void setCreated(ZonedDateTime created) {
		this.created = created;
	}

	public ZonedDateTime getEdited() {
		return edited;
	}

	public void setEdited(ZonedDateTime edited) {
		this.edited = edited;
	}

	public Integer getEditCount() {
		return editCount;
	}

	public void setEditCount(Integer editCount) {
		this.editCount = editCount;
	}
}
