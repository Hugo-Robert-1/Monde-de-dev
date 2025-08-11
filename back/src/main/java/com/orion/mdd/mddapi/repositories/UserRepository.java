package com.orion.mdd.mddapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orion.mdd.mddapi.models.User;

/**
 * Repository JPA pour l'entité {@link com.orion.mdd.mddapi.models.User}.
 * 
 * Fournit des méthodes pour récupérer un utilisateur via son email ou son nom
 * d'utilisateur.
 */
public interface UserRepository extends JpaRepository<User, Long> {
	/**
	 * Recherche un utilisateur par son adresse email.
	 * 
	 * @param email adresse email
	 * @return utilisateur correspondant ou null si non trouvé
	 */
	User findByEmail(String email);

	/**
	 * Recherche un utilisateur par son nom d'utilisateur.
	 * 
	 * @param username nom d'utilisateur
	 * @return utilisateur correspondant ou null si non trouvé
	 */
	User findByUsername(String username);
}
