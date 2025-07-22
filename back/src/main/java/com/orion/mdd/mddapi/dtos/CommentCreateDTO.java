package com.orion.mdd.mddapi.dtos;

public record CommentCreateDTO(
		String content,
		Long postId) {
};