package com.orion.mdd.mddapi.dtos;

/**
 * DTO de requête pour créer un nouvel article.
 *
 * @param title     titre de l'article, non nul, max 255 caractères
 * @param content   contenu de l'article
 * @param subjectId identifiant du thème lié
 */
public record PostCreateDTO(
		String title,
		String content,
		Long subjectId) {
}