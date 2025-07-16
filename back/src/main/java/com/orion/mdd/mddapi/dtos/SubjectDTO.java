package com.orion.mdd.mddapi.dtos;

import java.time.LocalDateTime;

public record SubjectDTO(
		Long id,
		String name,
		String description,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {
}