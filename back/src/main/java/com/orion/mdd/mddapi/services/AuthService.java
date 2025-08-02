package com.orion.mdd.mddapi.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.orion.mdd.mddapi.dtos.AuthDTO;
import com.orion.mdd.mddapi.dtos.TokenRefreshRequestDTO;
import com.orion.mdd.mddapi.dtos.UserDTO;
import com.orion.mdd.mddapi.models.RefreshToken;
import com.orion.mdd.mddapi.models.User;
import com.orion.mdd.mddapi.payload.request.LoginRequest;
import com.orion.mdd.mddapi.payload.request.RegisterRequest;
import com.orion.mdd.mddapi.repositories.UserRepository;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JWTService jwtService;

	@Autowired
	private RefreshTokenService refreshTokenService;

	/**
	 * Create a new user and return a jwt token linked to that new user
	 * 
	 * @param request
	 * @return AuthDTO
	 */
	public AuthDTO register(RegisterRequest request) {
		/**
		 * if (!Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).+$",
		 * request.getPassword())) { throw new BadCredentialsException( "Le mot de passe
		 * doit contenir au moins une majuscule, une minuscule, un chiffre et un
		 * caractère spécial."); }
		 **/

		User user = new User();
		user.setUsername(request.username());
		user.setEmail(request.email());
		user.setPassword(passwordEncoder.encode(request.password()));

		LocalDateTime date = LocalDateTime.now();
		user.setCreatedAt(date);
		user.setUpdatedAt(date);

		userRepository.save(user);

		String jwt = jwtService.generateToken(user.getUsername());
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
		return new AuthDTO(jwt, refreshToken.getToken());
	}

	/**
	 * Find a user by his email and check credentials if matching, return a jwt
	 * token else return Exceptions
	 * 
	 * @param request
	 * @return AuthDTO
	 */
	public AuthDTO login(LoginRequest request) {
		User user = loadUserByIdentifier(request.identifier());

		if (user == null) {
			throw new UsernameNotFoundException("Utilisateur introuvable");
		}

		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new BadCredentialsException("Mot de passe invalide");
		}

		String jwt = jwtService.generateToken(user.getUsername());

		RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
		return new AuthDTO(jwt, refreshToken.getToken());
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

	public User findUserByIdentifier(String identifier) {
		User user = userRepository.findByEmail(identifier);
		if (user == null) {
			user = userRepository.findByUsername(identifier);
		}
		if (user == null) {
			throw new UsernameNotFoundException("Utilisateur non trouvé avec l'identifiant : " + identifier);
		}
		return user;
	}

	public AuthDTO refreshAccessToken(TokenRefreshRequestDTO request) {
		String refreshToken = request.refreshToken();

		RefreshToken token = refreshTokenService.findByToken(refreshToken)
				.orElseThrow(() -> new RuntimeException("Refresh token not found"));

		refreshTokenService.verifyExpiration(token);

		String newAccessToken = jwtService.generateToken(token.getUser().getUsername());
		return new AuthDTO(newAccessToken, refreshToken);
	}
}
