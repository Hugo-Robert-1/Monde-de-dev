package com.orion.mdd.mddapi.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record PostWithCommentsDTO(
		Long id,
		String title,
		String content,
		SubjectLightDTO subject,
		LocalDateTime createdAt,
		UserLightDTO author,
		List<CommentDTO> commentaires) {
}