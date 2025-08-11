package com.orion.mdd.mddapi.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Représente un article publié par un utilisateur et associé à un thème.
 * <p>
 * Contient le titre, le contenu, les dates de création et modification, ainsi
 * que les relations vers le {@link Subject} et l'auteur {@link User}. Un
 * article peut avoir plusieurs commentaires {@link Comment}.
 * </p>
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "posts")
public class Post {

	/** Identifiant unique de l'article (clé primaire). */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Titre de l'article. */
	private String title;

	/** Contenu de l'article, de type texte long (LOB). */
	@Lob
	private String content;

	/** Date et heure de création, générée automatiquement. */
	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	/** Date et heure de la dernière modification, générée automatiquement. */
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	/** Thème auquel l'article appartient. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_id", nullable = false)
	private Subject subject;

	/** Utilisateur auteur de l'article. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User author;

	/** Liste des commentaires associés à cet article. */
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Comment> comments = new ArrayList<>();
}
