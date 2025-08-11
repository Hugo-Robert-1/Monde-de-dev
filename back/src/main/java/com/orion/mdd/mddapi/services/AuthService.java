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

/**
 * Service gérant l'authentification et la gestion des utilisateurs.
 */
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
	 * Enregistre un nouvel utilisateur et génère un token JWT associé.
	 *
	 * @param request données d'inscription
	 * @return {@link AuthDTO} contenant le token JWT et le refresh token
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
	 * Authentifie un utilisateur et retourne un token JWT s'il est valide.
	 *
	 * @param request données de connexion
	 * @return {@link AuthDTO} contenant le token JWT et le refresh token
	 * @throws UsernameNotFoundException si l'utilisateur n'existe pas
	 * @throws BadCredentialsException   si le mot de passe est invalide
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

	/**
	 * Charge un utilisateur à partir de son identifiant (email ou nom
	 * d'utilisateur).
	 *
	 * @param identifier identifiant de l'utilisateur
	 * @return {@link User} correspondant ou {@code null} si non trouvé
	 */
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

	/**
	 * Retourne les informations de l'utilisateur sous forme de DTO.
	 *
	 * @param user utilisateur
	 * @return {@link UserDTO}
	 */
	public UserDTO getCurrentUser(User user) {
		return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt(), user.getUpdatedAt());
	}

	/**
	 * Recherche un utilisateur par email.
	 *
	 * @param email email de l'utilisateur
	 * @return {@link User} trouvé
	 * @throws UsernameNotFoundException si l'utilisateur n'existe pas
	 */
	public User findUserByEmail(String email) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email);
		}
		return user;
	}

	/**
	 * Recherche un utilisateur par email ou nom d'utilisateur.
	 *
	 * @param identifier identifiant de l'utilisateur
	 * @return {@link User} trouvé
	 * @throws UsernameNotFoundException si l'utilisateur n'existe pas
	 */
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

	/**
	 * Génère un nouveau token d'accès à partir d'un refresh token valide.
	 *
	 * @param request requête contenant le refresh token
	 * @return {@link AuthDTO} contenant le nouveau token d'accès et le refresh
	 *         token
	 */
	public AuthDTO refreshAccessToken(TokenRefreshRequestDTO request) {
		String refreshToken = request.refreshToken();

		RefreshToken token = refreshTokenService.findByToken(refreshToken)
				.orElseThrow(() -> new RuntimeException("Refresh token not found"));

		refreshTokenService.verifyExpiration(token);

		String newAccessToken = jwtService.generateToken(token.getUser().getUsername());
		return new AuthDTO(newAccessToken, refreshToken);
	}
}
