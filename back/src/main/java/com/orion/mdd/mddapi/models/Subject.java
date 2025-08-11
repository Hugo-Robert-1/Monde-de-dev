package com.orion.mdd.mddapi.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Représente un thème ou une catégorie à laquelle un article peut appartenir.
 * <p>
 * Un thème contient un nom, une description, et peut représenter plusieurs
 * articles.
 * </p>
 */
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subjects")
public class Subject {

	/** Identifiant unique du thème (clé primaire). */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Nom du thème. */
	private String name;

	/** Description détaillée du thème (max 2000 caractères). */
	@Column(length = 2000)
	private String description;

	/** Date et heure de création du thème (auto-générée). */
	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	/** Date et heure de dernière modification du thème (auto-générée). */
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	/** Ensemble des articles rattachés à ce thème. */
	@OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Post> posts = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Subject subject = (Subject) o;
		return id != null && id.equals(subject.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
