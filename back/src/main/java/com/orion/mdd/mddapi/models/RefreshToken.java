package com.orion.mdd.mddapi.models;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Représente un token de rafraîchissement utilisé pour renouveler un token JWT
 * d'authentification.
 * <p>
 * Chaque token est associé à un utilisateur unique et possède une date
 * d'expiration.
 * </p>
 */
@Data
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

	/** Identifiant unique du token (clé primaire). */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Valeur unique du token de rafraîchissement. */
	@Column(nullable = false, unique = true)
	private String token;

	/** Utilisateur lié à ce token de rafraîchissement. */
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	/** Date d'expiration du token. */
	@Column(nullable = false)
	private Instant expiryDate;
}