package com.orion.mdd.mddapi.dtos;

/**
 * DTO utilisé pour mettre à jour les informations d'un utilisateur.
 *
 * @param username nouveau nom d'utilisateur
 * @param email    nouvelle adresse e-mail
 * @param password nouveau mot de passe
 */
public record UserUpdatedDTO(
		String username,
		String email,
		String password) {
}
