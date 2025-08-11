package com.orion.mdd.mddapi.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.orion.mdd.mddapi.models.Post;
import com.orion.mdd.mddapi.models.Subject;

/**
 * Repository JPA pour l'entité {@link com.orion.mdd.mddapi.models.Post}.
 * 
 * Fournit des méthodes pour gérer les articles et permettre des requêtes
 * personnalisées basées sur les thèmes associés.
 */
public interface PostRepository extends JpaRepository<Post, Long> {
	/**
	 * Recherche la liste des articles appartenant à un ensemble de thèmes donnés.
	 * 
	 * @param subjects ensemble de thèmes
	 * @return liste d'articles correspondant aux thèmes fournis
	 */
	List<Post> findBySubjectIn(Set<Subject> subjects);

	/**
	 * Recherche les articles appartenant à une liste d'IDs de thèmes, avec un tri.
	 * 
	 * @param subjectIds liste des IDs de thèmes
	 * @param sort       critère de tri (ex: par date, titre...)
	 * @return liste d'articles triée
	 */
	List<Post> findBySubjectIdIn(List<Long> subjectIds, Sort sort);
}
