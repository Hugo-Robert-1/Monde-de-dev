package com.orion.mdd.mddapi.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orion.mdd.mddapi.dtos.AuthDTO;
import com.orion.mdd.mddapi.dtos.TokenRefreshRequestDTO;
import com.orion.mdd.mddapi.dtos.UserDTO;
import com.orion.mdd.mddapi.models.User;
import com.orion.mdd.mddapi.payload.request.LoginRequest;
import com.orion.mdd.mddapi.payload.request.RegisterRequest;
import com.orion.mdd.mddapi.services.AuthService;

/**
 * Contrôleur REST gérant les opérations d'authentification et de gestion des
 * tokens JWT.
 * <p>
 * Ce contrôleur fournit des points d'entrée pour :
 * <ul>
 * <li>Inscription d'un nouvel utilisateur et génération d'un access token</li>
 * <li>Connexion et génération d'un access token</li>
 * <li>Récupération des informations de l'utilisateur courant</li>
 * <li>Renouvellement du token d'accès via un refresh token</li>
 * </ul>
 * Les refresh tokens sont envoyés et stockés sous forme de cookies HTTPOnly
 * pour plus de sécurité.
 * </p>
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	/**
	 * Durée de validité du refresh token en millisecondes, injectée depuis la
	 * configuration.
	 */
	@Value("${app.jwtRefreshExpirationMs}")
	private Long refreshTokenDurationMs;

	@Autowired
	private AuthService authService;

	/**
	 * Inscrit un nouvel utilisateur et génère un access token et un refresh token.
	 *
	 * @param request les informations nécessaires à l'inscription (email, mot de
	 *                passe, username)
	 * @return une réponse HTTP contenant l'access token dans le corps et le refresh
	 *         token en cookie sécurisé
	 */
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
		AuthDTO jwtResponse = authService.register(request);

		ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", jwtResponse.refreshToken())
				.httpOnly(true)
				.secure(true)
				.path("/api/auth/refresh-token")
				.maxAge(refreshTokenDurationMs / 1000) // ms to secondes
				.sameSite("Strict")
				.build();

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
				.body(Map.of("accessToken", jwtResponse.accessToken()));
	}

	/**
	 * Authentifie un utilisateur existant et génère un nouvel access token et un
	 * refresh token.
	 *
	 * @param request les identifiants de connexion (email/username et mot de passe)
	 * @return une réponse HTTP contenant l'access token dans le corps et le refresh
	 *         token en cookie sécurisé
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {
		AuthDTO jwtResponse = authService.login(request);

		ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", jwtResponse.refreshToken())
				.httpOnly(true)
				.secure(true)
				.path("/api/auth/refresh-token") // accessible uniquement par cette route
				.maxAge(refreshTokenDurationMs / 1000) // ms to secondes
				.sameSite("Strict")
				.build();

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
				.body(Map.of("accessToken", jwtResponse.accessToken()));
	}

	/**
	 * Récupère les informations de l'utilisateur actuellement authentifié.
	 *
	 * @param authentication objet Spring Security contenant les informations de
	 *                       l'utilisateur connecté
	 * @return un {@link UserDTO} représentant l'utilisateur courant
	 */
	@GetMapping("/me")
	public ResponseEntity<UserDTO> me(Authentication authentication) {
		String identifier = authentication.getName(); // peut être un email ou un username
		User user = authService.findUserByIdentifier(identifier);
		return ResponseEntity.ok(authService.getCurrentUser(user));
	}

	/**
	 * Renouvelle l'access token à partir d'un refresh token stocké en cookie
	 * sécurisé.
	 *
	 * @param refreshToken le refresh token extrait du cookie HTTPOnly
	 * @return une réponse HTTP contenant un nouvel access token et un nouveau
	 *         refresh token
	 */
	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(@CookieValue("refreshToken") String refreshToken) {
		TokenRefreshRequestDTO requestDTO = new TokenRefreshRequestDTO(refreshToken);
		AuthDTO jwtResponse = authService.refreshAccessToken(requestDTO);

		ResponseCookie newRefreshTokenCookie = ResponseCookie.from("refreshToken", jwtResponse.refreshToken())
				.httpOnly(true)
				.secure(true)
				.path("/api/auth/refresh-token")
				.maxAge(refreshTokenDurationMs / 1000) // ms to secondes
				.sameSite("Strict")
				.build();

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString())
				.body(Map.of("accessToken", jwtResponse.accessToken()));
	}
}
