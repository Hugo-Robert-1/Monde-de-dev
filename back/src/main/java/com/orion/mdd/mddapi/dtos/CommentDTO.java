package com.orion.mdd.mddapi.dtos;

import java.time.LocalDateTime;

/**
 * Représente un commentaire
 * 
 * @param id        du commentaire
 * @param content   contenue du commentaire
 * @param createdAt date de création du commentaire
 * @param postId    identifiant de l'article sur lequel le commentaire est posté
 */
public record CommentDTO(
		Long id,
		String content,
		LocalDateTime createdAt,
		UserLightDTO user) {
}