package com.orion.mdd.mddapi.payload.request;

public record LoginRequest(
		String identifier,
		String password) {
}
