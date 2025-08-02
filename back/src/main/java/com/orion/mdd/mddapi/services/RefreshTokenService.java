package com.orion.mdd.mddapi.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.orion.mdd.mddapi.models.RefreshToken;
import com.orion.mdd.mddapi.repositories.RefreshTokenRepository;
import com.orion.mdd.mddapi.repositories.UserRepository;

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

	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().isBefore(Instant.now())) {
			refreshTokenRepository.delete(token);
			throw new RuntimeException("Refresh token expired. Please login again.");
		}
		return token;
	}

	public void deleteByUserId(Long userId) {
		refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
	}
}