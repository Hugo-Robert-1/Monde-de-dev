package com.orion.mdd.mddapi.dtos;

import java.time.LocalDateTime;

public record UserDTO(
		Long id,
		String username,
		String email,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {
}
