package com.orion.mdd.mddapi.services;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.orion.mdd.mddapi.security.KeyUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * Service pour la gestion des tokens JWT.
 * <p>
 * Fournit la génération, validation et extraction des informations d'un JWT.
 * Utilise une clé RSA privée/publique pour signer et vérifier les tokens.
 * </p>
 */
@Service
public class JWTService {

	@Value("${security.jwt.expiration-time}")
	private long expirationTime;

	/**
	 * Charge la clé privée pour signer les tokens.
	 *
	 * @return la clé privée RSA
	 * @throws Exception en cas d'erreur de chargement
	 */
	private PrivateKey getPrivateKey() throws Exception {
		return KeyUtils.loadPrivateKey();
	}

	/**
	 * Charge la clé publique pour vérifier les tokens.
	 *
	 * @return la clé publique RSA
	 * @throws Exception en cas d'erreur de chargement
	 */
	private PublicKey getPublicKey() throws Exception {
		return KeyUtils.loadPublicKey();
	}

	/**
	 * Génère un token JWT signé pour un nom d'utilisateur donné.
	 *
	 * @param username nom d'utilisateur à insérer dans le token
	 * @return token JWT signé
	 */
	public String generateToken(String username) {
		try {
			Instant now = Instant.now();
			return Jwts.builder()
					.claim("sub", username)
					.claim("iat", Date.from(now))
					.claim("exp", Date.from(now.plusMillis(expirationTime)))
					.signWith(getPrivateKey(), io.jsonwebtoken.SignatureAlgorithm.RS256)
					.compact();
		} catch (Exception e) {
			throw new RuntimeException("Error generating JWT token", e);
		}
	}

	/**
	 * Valide un token JWT en vérifiant son utilisateur et son expiration.
	 *
	 * @param token       token JWT à valider
	 * @param userDetails détails de l'utilisateur pour vérification
	 * @return true si le token est valide, false sinon
	 */
	public boolean validateToken(String token, UserDetails userDetails) {
		final String userName = extractIdentifier(token);
		return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	/**
	 * Vérifie la validité du token en vérifiant son utilisateur et son expiration.
	 *
	 * @param token       token JWT
	 * @param userDetails détails utilisateur
	 * @return true si valide, false sinon
	 */
	public boolean isTokenValid(String token, UserDetails userDetails) {
		String identifier = extractIdentifier(token);
		return identifier != null && identifier.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public String extractEmail(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public String extractIdentifier(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	/**
	 * Extrait une réclamation spécifique du token.
	 *
	 * @param token         token JWT
	 * @param claimResolver fonction pour extraire la réclamation
	 * @param <T>           type de la réclamation
	 * @return valeur extraite
	 */
	private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}

	/**
	 * Extrait toutes les réclamations d'un token JWT en le décodant.
	 *
	 * @param token token JWT
	 * @return objet Claims contenant toutes les données du token
	 */
	private Claims extractAllClaims(String token) {
		try {
			return Jwts.parser()
					.verifyWith(getPublicKey())
					.build()
					.parseSignedClaims(token)
					.getPayload();
		} catch (Exception e) {
			throw new RuntimeException("Error extracting claims from JWT token", e);
		}
	}
}