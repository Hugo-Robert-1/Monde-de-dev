package com.orion.mdd.mddapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orion.mdd.mddapi.models.Subject;

/**
 * Repository JPA pour l'entité {@link com.orion.mdd.mddapi.models.Subject}.
 * 
 * Fournit les opérations CRUD de base sur les thèmes.
 */
public interface SubjectRepository extends JpaRepository<Subject, Long> {

}
