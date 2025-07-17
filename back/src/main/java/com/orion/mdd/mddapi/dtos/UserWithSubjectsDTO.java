package com.orion.mdd.mddapi.dtos;

import java.util.Set;

public record UserWithSubjectsDTO(
		Long id,
		String username,
		String email,
		Set<SubjectDTO> subscribedSubjects) {
}
