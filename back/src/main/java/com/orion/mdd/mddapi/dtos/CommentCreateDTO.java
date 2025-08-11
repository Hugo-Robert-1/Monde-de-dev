package com.orion.mdd.mddapi.dtos;

/**
 * DTO de requête pour créer un commentaire
 * 
 * @param content contenue du commentaire
 * @param postId  identifiant de l'article sur lequel le commentaire est posté
 */
public record CommentCreateDTO(
		String content,
		Long postId) {
};