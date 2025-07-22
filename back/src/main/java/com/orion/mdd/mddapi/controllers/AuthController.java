package com.orion.mdd.mddapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orion.mdd.mddapi.dtos.AuthDTO;
import com.orion.mdd.mddapi.dtos.UserDTO;
import com.orion.mdd.mddapi.models.User;
import com.orion.mdd.mddapi.payload.request.LoginRequest;
import com.orion.mdd.mddapi.payload.request.RegisterRequest;
import com.orion.mdd.mddapi.services.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<AuthDTO> register(@RequestBody RegisterRequest request) {
		return ResponseEntity.ok(authService.register(request));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthDTO> login(@RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@GetMapping("/me")
	public ResponseEntity<UserDTO> me() {
		User user = authService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
		return ResponseEntity.ok(authService.getCurrentUser(user));
	}
}
