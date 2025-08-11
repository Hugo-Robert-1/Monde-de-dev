package com.orion.mdd.mddapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orion.mdd.mddapi.models.Comment;

/**
 * Repository JPA pour l'entité {@link com.orion.mdd.mddapi.models.Comment}.
 * 
 * Fournit les opérations CRUD de base sur les commentaires.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
