package com.orion.mdd.mddapi.services;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé : " + email);
        }

        return new org.springframework.security.core.userdetails.User(
        		user.getEmail(), 
				user.getPassword(),
				Collections.emptyList()
		);
    }
}
