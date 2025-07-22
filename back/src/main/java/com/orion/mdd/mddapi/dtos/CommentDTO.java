package com.orion.mdd.mddapi.dtos;

import java.time.LocalDateTime;

public record CommentDTO(
		Long id,
		String content,
		LocalDateTime createdAt,
		UserLightDTO user) {
}