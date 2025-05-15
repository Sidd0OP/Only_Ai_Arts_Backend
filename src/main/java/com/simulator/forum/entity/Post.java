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
@Table(name = "post")
@Getter
@Setter
public class Post {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private long userId;
	
	private String title;
	
	private String body;
	
	private ZonedDateTime created;
	
	private ZonedDateTime edited;
	
	private Integer editCount;
	
	private Integer commentCount;
	
	private String imageUrl;

}
