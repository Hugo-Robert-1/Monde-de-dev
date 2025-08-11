package com.orion.mdd.mddapi.dtos;

/**
 * DTO ayant pour objectif de stocker les 2 tokens JWT (accès et refresh)
 * 
 * @param accessToken  Token d'accès JWT (courte durée de vie)
 * @param refreshToken Token de rafraichissment (longue durée de vie)
 */
public record AuthDTO(
		String accessToken,
		String refreshToken) {
}
