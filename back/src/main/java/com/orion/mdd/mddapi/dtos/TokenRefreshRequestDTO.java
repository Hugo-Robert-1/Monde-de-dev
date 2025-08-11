package com.orion.mdd.mddapi.dtos;

/**
 * Requête pour rafraîchir un jeton d'accès (access token) via un refresh token.
 *
 * @param refreshToken jeton de rafraîchissement valide
 */
public record TokenRefreshRequestDTO(
		String refreshToken) {
};
