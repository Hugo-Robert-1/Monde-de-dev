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

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Value("${app.jwtRefreshExpirationMs}")
	private Long refreshTokenDurationMs;

	@Autowired
	private AuthService authService;

	/**
	 * @PostMapping("/register") public ResponseEntity<AuthDTO>
	 * register(@RequestBody RegisterRequest request) { return
	 * ResponseEntity.ok(authService.register(request)); }
	 **/
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
	 * @PostMapping("/login") public ResponseEntity<AuthDTO> login(@RequestBody
	 * LoginRequest request) { return ResponseEntity.ok(authService.login(request));
	 * }
	 **/
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

	@GetMapping("/me")
	public ResponseEntity<UserDTO> me(Authentication authentication) {
		String identifier = authentication.getName(); // peut être un email ou un username
		User user = authService.findUserByIdentifier(identifier);
		return ResponseEntity.ok(authService.getCurrentUser(user));
	}

	/**
	 * @PostMapping("/refresh-token") public ResponseEntity<?>
	 * refreshToken(@RequestBody TokenRefreshRequestDTO request) { return
	 * ResponseEntity.ok(authService.refreshAccessToken(request)); }
	 **/
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
