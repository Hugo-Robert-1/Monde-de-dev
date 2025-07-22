package com.orion.mdd.mddapi.dtos;

import java.time.LocalDateTime;

public record PostDTO(
		Long id,
		String title,
		String content,
		SubjectLightDTO subject,
		LocalDateTime createdAt,
		UserLightDTO author) {
}
