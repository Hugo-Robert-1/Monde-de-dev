package com.orion.mdd.mddapi.dtos;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de requête pour afficher un article avec les commentaires associés ainsi
 * que la date d'écriture, le thème de l'article et l'autheur.
 *
 * @param id           identifiant de l'article
 * @param title        titre de l'article, non nul, max 255 caractères
 * @param content      contenu de l'article
 * @param subjectId    identifiant du thème lié
 * @param createdAt    date de création
 * @param author       les données relatifs à l'autheur de l'article
 * @param commentaires liste des commentaires associées à l'article
 */
public record PostWithCommentsDTO(
		Long id,
		String title,
		String content,
		SubjectLightDTO subject,
		LocalDateTime createdAt,
		UserLightDTO author,
		List<CommentDTO> commentaires) {
}