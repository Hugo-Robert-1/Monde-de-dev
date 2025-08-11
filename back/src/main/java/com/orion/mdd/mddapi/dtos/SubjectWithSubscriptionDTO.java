package com.orion.mdd.mddapi.dtos;

/**
 * Représente un thème avec l'état d'abonnement de l'utilisateur courant.
 *
 * @param id           identifiant unique du thème
 * @param name         nom du thème
 * @param description  description détaillée du thème
 * @param isSubscribed vrai si l'utilisateur est abonné à ce thème, faux sinon
 */
public record SubjectWithSubscriptionDTO(
		Long id,
		String name,
		String description,
		boolean isSubscribed) {
}
