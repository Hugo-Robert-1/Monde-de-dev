package com.orion.mdd.mddapi.dtos;

public record PostCreateDTO(
		String title,
		String content,
		Long subjectId) {
}