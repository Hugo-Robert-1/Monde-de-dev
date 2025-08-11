package com.orion.mdd.mddapi.dtos;

import java.time.LocalDateTime;

/**
 * Représente un utilisateur complet avec ses métadonnées.
 *
 * @param id        identifiant unique de l'utilisateur
 * @param username  nom d'utilisateur unique
 * @param email     adresse e-mail de l'utilisateur
 * @param createdAt date et heure de création du compte
 * @param updatedAt date et heure de dernière modification du compte
 */
public record UserDTO(
		Long id,
		String username,
		String email,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {
}
