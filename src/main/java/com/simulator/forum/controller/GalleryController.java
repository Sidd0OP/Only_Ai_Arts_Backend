package com.simulator.forum.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.simulator.forum.dto.snippet.GallerySnippet;
import com.simulator.forum.repository.PostRepository;

@RestController
public class GalleryController {
	
	@Autowired
	private PostRepository postRepository;
	
	@GetMapping("/gallery")
	public ResponseEntity<?> getImages()
	{
		List<GallerySnippet> cards = postRepository.getGallerySnippets(0);
		
		
		return new ResponseEntity<>(cards , HttpStatus.OK);
		
	}
	
	
	@GetMapping("/gallery/{page}")
	public ResponseEntity<?> getMoreImages(@PathVariable String page)
	{
		int pageNumber;
		
		try 
		{
			pageNumber = Integer.valueOf(page) * 40;
			
		}catch(NumberFormatException e) {
			
			return new ResponseEntity<>("numeric parameter required"  , HttpStatus.BAD_REQUEST);
		}
		
		List<GallerySnippet> cards = postRepository.getGallerySnippets(pageNumber);
		
		
		return new ResponseEntity<>(cards , HttpStatus.OK);
		
	}
	
	
	

}
