package com.orion.mdd.mddapi.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.orion.mdd.mddapi.models.RefreshToken;
import com.orion.mdd.mddapi.models.User;

/**
 * Repository JPA pour l'entité
 * {@link com.orion.mdd.mddapi.models.RefreshToken}.
 * 
 * Fournit des opérations CRUD ainsi que des méthodes pour manipuler les tokens
 * de rafraîchissement liés aux utilisateurs.
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	/**
	 * Recherche un token de rafraîchissement par sa valeur unique.
	 * 
	 * @param token chaîne du token
	 * @return Optional contenant le RefreshToken si trouvé, sinon vide
	 */
	Optional<RefreshToken> findByToken(String token);

	/**
	 * Supprime tous les tokens de rafraîchissement associés à un utilisateur donné.
	 * 
	 * @param user utilisateur dont on supprime les tokens
	 */
	@Modifying
	@Transactional
	void deleteByUser(User user);

	/**
	 * Supprime tous les tokens de rafraîchissement associés à un utilisateur via
	 * son ID.
	 * 
	 * @param userId ID de l'utilisateur
	 */
	@Modifying
	@Transactional
	@Query("DELETE FROM RefreshToken r WHERE r.user.id = :userId")
	void deleteByUserId(@Param("userId") Long userId);
}
