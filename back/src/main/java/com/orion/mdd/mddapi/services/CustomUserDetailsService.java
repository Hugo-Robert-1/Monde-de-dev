package com.orion.mdd.mddapi.services;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.orion.mdd.mddapi.models.User;
import com.orion.mdd.mddapi.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

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
