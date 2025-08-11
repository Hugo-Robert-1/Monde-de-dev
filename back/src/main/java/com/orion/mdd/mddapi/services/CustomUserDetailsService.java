package com.orion.mdd.mddapi.services;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.orion.mdd.mddapi.models.User;
import com.orion.mdd.mddapi.repositories.UserRepository;

/**
 * Service implémentant UserDetailsService pour Spring Security.
 * <p>
 * Permet de charger un utilisateur à partir de son identifiant, qui peut être
 * un email ou un username. Utilisé pour l'authentification.
 * </p>
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Charge un utilisateur à partir de son identifiant (email ou username).
	 *
	 * @param identifier email ou username de l'utilisateur
	 * @return les détails de l'utilisateur nécessaires à Spring Security
	 * @throws UsernameNotFoundException si l'utilisateur n'existe pas
	 */
	@Override
	public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
		User user = isEmail(identifier)
				? userRepository.findByEmail(identifier)
				: userRepository.findByUsername(identifier);

		if (user == null) {
			throw new UsernameNotFoundException("Utilisateur non trouvé : " + identifier);
		}

		return new org.springframework.security.core.userdetails.User(
				user.getUsername(),
				user.getPassword(),
				Collections.emptyList());
	}

	private boolean isEmail(String identifier) {
		return identifier.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
	}
}
