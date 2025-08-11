package com.orion.mdd.mddapi.dtos;

import java.util.Set;

/**
 * Représente un utilisateur avec la liste de ses thèmes abonnés.
 *
 * @param id                 identifiant unique de l'utilisateur
 * @param username           nom d'utilisateur
 * @param email              adresse e-mail de l'utilisateur
 * @param subscribedSubjects ensemble des thèmes auxquels l'utilisateur est
 *                           abonné
 */
public record UserWithSubjectsDTO(
		Long id,
		String username,
		String email,
		Set<SubjectDTO> subscribedSubjects) {
}
