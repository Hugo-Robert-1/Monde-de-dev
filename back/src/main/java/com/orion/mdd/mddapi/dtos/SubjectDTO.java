package com.orion.mdd.mddapi.dtos;

import java.time.LocalDateTime;

/**
 * Représente un thème complet avec ses métadonnées.
 *
 * @param id          identifiant du thème
 * @param name        nom du thème
 * @param description description détaillée du thème
 * @param createdAt   date et heure de création du thème
 * @param updatedAt   date et heure de dernière modification du thème
 */
public record SubjectDTO(
		Long id,
		String name,
		String description,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {
}