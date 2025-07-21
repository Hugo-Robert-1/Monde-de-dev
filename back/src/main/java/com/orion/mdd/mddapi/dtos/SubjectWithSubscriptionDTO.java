package com.orion.mdd.mddapi.dtos;

public record SubjectWithSubscriptionDTO(
		Long id,
		String name,
		String description,
		boolean isSubscribed) {
}
