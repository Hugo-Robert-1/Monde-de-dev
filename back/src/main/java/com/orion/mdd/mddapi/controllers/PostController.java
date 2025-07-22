package com.orion.mdd.mddapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orion.mdd.mddapi.dtos.PostCreateDTO;
import com.orion.mdd.mddapi.dtos.PostDTO;
import com.orion.mdd.mddapi.mapper.PostMapper;
import com.orion.mdd.mddapi.models.Post;
import com.orion.mdd.mddapi.models.User;
import com.orion.mdd.mddapi.services.AuthService;
import com.orion.mdd.mddapi.services.PostService;

@RestController
@RequestMapping("/api/post")
public class PostController {
	@Autowired
	private PostMapper postMapper;

	@Autowired
	private PostService postService;

	@Autowired
	private AuthService authService;

	@PostMapping
	public ResponseEntity<?> create(@RequestBody PostCreateDTO postDto) {
		User user = authService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

		this.postService.create(postDto, user);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
		Post post = postService.findById(id);
		PostDTO postDTO = postMapper.toDto(post);
		return ResponseEntity.ok(postDTO);
	}

}
