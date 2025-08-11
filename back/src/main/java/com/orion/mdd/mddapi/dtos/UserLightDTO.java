package com.orion.mdd.mddapi.dtos;

/**
 * Représente un utilisateur allégé, ne contenant que les informations
 * essentielles.
 *
 * @param id       identifiant unique de l'utilisateur
 * @param username nom d'utilisateur
 */
public record UserLightDTO(
		Long id,
		String username) {
}
