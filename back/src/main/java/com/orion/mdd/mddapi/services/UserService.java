package com.orion.mdd.mddapi.services;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.orion.mdd.mddapi.models.User;
import com.orion.mdd.mddapi.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JWTService tokenGenerator;

	/**
	 * Get a user by his ID
	 * 
	 * @param id The ID of the user to retrieve
	 * @return User The user corresponding to the ID
	 */
	public User getUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID : " + id));
		return user;
	}

	/**
	 * Update the current user with new values, and do nothing if values are the
	 * same
	 * 
	 * @param user
	 * @param updatedUser
	 * @return
	 */
	public String update(User user, User updatedUser) {
		if (updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty()) {
			user.setUsername(updatedUser.getUsername());
		}
		if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
			if (!(updatedUser.getEmail()).matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
				throw new IllegalArgumentException("Invalid email address format.");
			}
			user.setEmail(updatedUser.getEmail());
		}

		if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
			if (!Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).+$", updatedUser.getPassword())) {
				throw new IllegalArgumentException("New password is not valid.");
			}
			if (!passwordEncoder.matches(updatedUser.getPassword(), user.getPassword())) {
				String hashedPassword = passwordEncoder.encode(updatedUser.getPassword());
				user.setPassword(hashedPassword);
			}
		}

		this.userRepository.save(user);

		return tokenGenerator.generateToken(user.getEmail());
	}
}
