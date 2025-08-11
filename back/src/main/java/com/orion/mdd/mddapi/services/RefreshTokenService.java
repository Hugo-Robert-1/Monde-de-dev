package com.orion.mdd.mddapi.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.orion.mdd.mddapi.models.RefreshToken;
import com.orion.mdd.mddapi.repositories.RefreshTokenRepository;
import com.orion.mdd.mddapi.repositories.UserRepository;

/**
 * Service pour la gestion des tokens de rafraîchissement (refresh tokens).
 * <p>
 * Permet la création, la recherche, la vérification d'expiration et la
 * suppression des refresh tokens.
 * </p>
 */
@Service
public class RefreshTokenService {

	@Value("${app.jwtRefreshExpirationMs}")
	private Long refreshTokenDurationMs;

	private final RefreshTokenRepository refreshTokenRepository;
	private final UserRepository userRepository;

	public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
		this.refreshTokenRepository = refreshTokenRepository;
		this.userRepository = userRepository;
	}

	/**
	 * Crée un nouveau refresh token pour un utilisateur donné. Supprime le token
	 * précédent s'il existe.
	 *
	 * @param userId identifiant de l'utilisateur
	 * @return refresh token créé et sauvegardé
	 */
	public RefreshToken createRefreshToken(Long userId) {
		// Delete previous token if existing
		// refreshTokenRepository.deleteByUserId(userId);
		this.deleteByUserId(userId);

		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setUser(userRepository.findById(userId).get());
		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
		refreshToken.setToken(UUID.randomUUID().toString());
		return refreshTokenRepository.save(refreshToken);
	}

	/**
	 * Recherche un refresh token par sa valeur token.
	 *
	 * @param token valeur du refresh token
	 * @return Optional contenant le refresh token si trouvé
	 */
	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	/**
	 * Vérifie que le refresh token n'est pas expiré. Supprime le token expiré et
	 * lance une exception si expiré.
	 *
	 * @param token refresh token à vérifier
	 * @return refresh token si valide
	 * @throws RuntimeException si le token est expiré
	 */
	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().isBefore(Instant.now())) {
			refreshTokenRepository.delete(token);
			throw new RuntimeException("Refresh token expired. Please login again.");
		}
		return token;
	}

	/**
	 * Supprime les refresh tokens associés à un utilisateur.
	 *
	 * @param userId identifiant de l'utilisateur
	 */
	public void deleteByUserId(Long userId) {
		refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
	}
}