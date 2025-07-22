package com.orion.mdd.mddapi.payload.request;

public record RegisterRequest(
		String username,
		String email,
		String password) {
}
