package com.orion.mdd.mddapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orion.mdd.mddapi.dtos.CommentCreateDTO;
import com.orion.mdd.mddapi.dtos.CommentDTO;
import com.orion.mdd.mddapi.mapper.CommentMapper;
import com.orion.mdd.mddapi.models.Comment;
import com.orion.mdd.mddapi.models.User;
import com.orion.mdd.mddapi.services.AuthService;
import com.orion.mdd.mddapi.services.CommentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

	@Autowired
	private CommentService commentService;

	@Autowired
	private CommentMapper commentMapper;

	@Autowired
	private AuthService authService;

	@PostMapping
	public ResponseEntity<CommentDTO> createComment(@RequestBody @Valid CommentCreateDTO dto) {
		User user = authService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		Comment saved = commentService.createComment(dto, user);
		return ResponseEntity.ok(commentMapper.toDto(saved));
	}
}
