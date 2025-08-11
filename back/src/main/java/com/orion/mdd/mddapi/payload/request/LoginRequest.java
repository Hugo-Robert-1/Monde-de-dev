package com.orion.mdd.mddapi.payload.request;

/**
 * Payload Login request pour envoyer les infos de connexion
 * 
 * @param identifier email ou username
 * @param password   mot de passe
 */
public record LoginRequest(
		String identifier,
		String password) {
}
