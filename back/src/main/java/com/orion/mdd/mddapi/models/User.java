package com.orion.mdd.mddapi.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Représente un utilisateur du système.
 * <p>
 * Contient les informations de connexion, les dates de création et
 * modification, ainsi que les thèmes auxquels l'utilisateur est abonné.
 * </p>
 */
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "USERS", uniqueConstraints = {
		@UniqueConstraint(columnNames = "email")
})
@NoArgsConstructor
public class User {

	/** Identifiant unique de l'utilisateur (clé primaire). */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Adresse email unique de l'utilisateur. */
	@Column(unique = true, length = 255)
	private String email;

	/** Nom d'utilisateur unique. */
	@Column(unique = true, length = 255)
	private String username;

	/** Mot de passe hashé de l'utilisateur. */
	@Column(length = 255)
	private String password;

	/** Date et heure de création de l'utilisateur (auto-générée). */
	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	/** Date et heure de la dernière modification (auto-générée). */
	@UpdateTimestamp
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	/** Ensemble des thèmes auxquels l'utilisateur est abonné. */
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "SUBSCRIPTION", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "subject_id"))
	private Set<Subject> subscribedSubjects = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		User user = (User) o;
		return id != null && id.equals(user.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
