package com.orion.mdd.mddapi.dtos;

/**
 * Représente un thème allégé, ne contenant que les informations essentielles.
 *
 * @param id   identifiant unique du thème
 * @param name nom du thème
 */
public record SubjectLightDTO(
		Long id,
		String name) {
}