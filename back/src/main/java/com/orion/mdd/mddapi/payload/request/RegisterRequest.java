package com.orion.mdd.mddapi.payload.request;

/**
 * Payload Register request pour envoyer les infos de création d'un compte
 * 
 * @param email    email
 * @param username username
 * @param password mot de passe
 */
public record RegisterRequest(
		String username,
		String email,
		String password) {
}
