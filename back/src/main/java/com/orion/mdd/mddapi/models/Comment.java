package com.orion.mdd.mddapi.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Représente un commentaire associé à un {@link Post} et publié par un
 * {@link User}.
 * <p>
 * Cette entité est persistée dans la table {@code comments} et contient le
 * contenu du commentaire, la date de création et de mise à jour, ainsi que les
 * relations vers l'article et l'utilisateur qui l'a rédigé.
 * </p>
 *
 * <ul>
 * <li>{@code id} — Identifiant unique du commentaire.</li>
 * <li>{@code content} — Texte du commentaire (max. 2000 caractères).</li>
 * <li>{@code createdAt} — Date et heure de création, automatiquement définie à
 * l'insertion.</li>
 * <li>{@code updatedAt} — Date et heure de la dernière modification,
 * automatiquement mise à jour.</li>
 * <li>{@code post} — Article auquel le commentaire est rattaché.</li>
 * <li>{@code user} — Utilisateur ayant rédigé le commentaire.</li>
 * </ul>
 *
 * Les dates {@code createdAt} et {@code updatedAt} sont gérées automatiquement
 * grâce aux annotations Hibernate
 * {@link org.hibernate.annotations.CreationTimestamp} et
 * {@link org.hibernate.annotations.UpdateTimestamp}.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment {

	/** Identifiant unique du commentaire (clé primaire). */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Contenu du commentaire, limité à 2000 caractères. */
	@Column(length = 2000)
	private String content;

	/** Date et heure de création du commentaire (auto-générée). */
	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	/** Date et heure de la dernière modification (auto-générée). */
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	/** Article auquel ce commentaire est lié. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id", nullable = false)
	private Post post;

	/** Utilisateur ayant rédigé le commentaire. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	/**
	 * Définit automatiquement la date de création avant la persistance.
	 * <p>
	 * Bien que {@link org.hibernate.annotations.CreationTimestamp} gère déjà cette
	 * valeur, cette méthode garantit que {@code createdAt} est bien défini même en
	 * cas de configuration particulière.
	 * </p>
	 */
	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
	}
}
