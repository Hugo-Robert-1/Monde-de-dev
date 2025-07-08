package com.orion.mdd.mddapi.services;

import java.time.LocalDateTime;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.orion.mdd.mddapi.dtos.AuthDTO;
import com.orion.mdd.mddapi.dtos.LoginDTO;
import com.orion.mdd.mddapi.dtos.RegisterDTO;
import com.orion.mdd.mddapi.dtos.UserDTO;
import com.orion.mdd.mddapi.models.User;
import com.orion.mdd.mddapi.repositories.UserRepository;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JWTService jwtService;

	public AuthService(UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			JWTService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	/**
	 * Create a new user and return a jwt token linked to that new user
	 * 
	 * @param request
	 * @return AuthDTO
	 */
	public AuthDTO register(RegisterDTO request) {
		User user = new User();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		LocalDateTime date = LocalDateTime.now();
		user.setCreatedAt(date);
		user.setUpdatedAt(date);

		userRepository.save(user);

		String jwt = jwtService.generateToken(user.getEmail());
		return new AuthDTO(jwt);
	}

	/**
	 * Find a user by his email and check credentials if matching, return a jwt
	 * token else return Exceptions
	 * 
	 * @param request
	 * @return AuthDTO
	 */
	public AuthDTO login(LoginDTO request) {
		User user = loadUserByIdentifier(request.getIdentifier());

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new BadCredentialsException("Mot de passe invalide");
		}

		String jwt = jwtService.generateToken(user.getEmail());

		return new AuthDTO(jwt);
	}

	public User loadUserByIdentifier(String identifier) {
		if (isEmail(identifier)) {
			return userRepository.findByEmail(identifier);
		} else {
			return userRepository.findByUsername(identifier);
		}
	}

	private boolean isEmail(String identifier) {
		return identifier.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
	}

	public UserDTO getCurrentUser(User user) {
		return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
	}

	/**
	 * Find a user by his email and return it
	 * 
	 * @param email
	 * @return user
	 */
	public User findUserByEmail(String email) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email);
		}
		return user;
	}
}
