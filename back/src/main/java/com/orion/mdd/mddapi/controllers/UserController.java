package com.orion.mdd.mddapi.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orion.mdd.mddapi.dtos.AuthDTO;
import com.orion.mdd.mddapi.dtos.UserDTO;
import com.orion.mdd.mddapi.dtos.UserUpdatedDTO;
import com.orion.mdd.mddapi.dtos.UserWithSubjectsDTO;
import com.orion.mdd.mddapi.mapper.UserMapper;
import com.orion.mdd.mddapi.models.User;
import com.orion.mdd.mddapi.services.AuthService;
import com.orion.mdd.mddapi.services.UserService;

/**
 * Contrôleur REST gérant les opérations liées à l'utilisateur.
 * <p>
 * Fournit des endpoints pour :
 * <ul>
 * <li>Récupérer un utilisateur par son identifiant</li>
 * <li>Mettre à jour les informations de l'utilisateur</li>
 * <li>Récupérer tous les thèmes auxquels l'utilsiateur est abonné</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

	@Value("${app.jwtRefreshExpirationMs}")
	private Long refreshTokenDurationMs;

	@Autowired
	private UserService userService;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private AuthService authService;

	/**
	 * Récupère un utilisateur via son id
	 * 
	 * @param id identifiant de l'utilisateur
	 * @return ResponseEntity<UserDTO>
	 */
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
		User user = userService.getUserById(id);
		UserDTO userDTO = userMapper.toDto(user);
		return ResponseEntity.ok(userDTO);
	}

	/**
	 * Récupère les infos de l'utilisateur ainsi que les thèmes auxquels il est
	 * abonné
	 * 
	 * @param id identifiant de l'utilisateur
	 * @return ResponseEntity<UserWithSubjectsDTO>
	 */
	@GetMapping("/{id}/subjects")
	public ResponseEntity<UserWithSubjectsDTO> getSubjectsForCurrentUser(@PathVariable Long id) {
		User user = userService.getUserById(id);

		UserWithSubjectsDTO dto = userMapper.toUserWithSubjectsDTO(user);
		return ResponseEntity.ok(dto);
	}

	/**
	 * Met à jour les infos de l'utilisateur
	 * 
	 * @param id             identifiant de l'utilisateur
	 * @param userUpdatedDto les informations modifiées par l'utilisateur
	 * @return une réponse HTTP contenant un nouvel access token dans le corps et le
	 *         refresh token en cookie sécurisé
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable String id, @RequestBody UserUpdatedDTO userUpdatedDto) {
		try {
			User user = authService
					.findUserByIdentifier(SecurityContextHolder.getContext().getAuthentication().getName());
			if (!user.getId().equals(Long.parseLong(id))) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body("You are not authorized to modify this user");
			}
			AuthDTO tokens = this.userService.update(user, this.userMapper.toEntity(userUpdatedDto));

			ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokens.refreshToken())
					.httpOnly(true)
					.secure(true)
					.path("/api/auth/refresh-token")
					.maxAge(refreshTokenDurationMs / 1000) // ms to secondes
					.sameSite("Strict")
					.build();

			return ResponseEntity.ok()
					.header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
					.body(Map.of("accessToken", tokens.accessToken()));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
}