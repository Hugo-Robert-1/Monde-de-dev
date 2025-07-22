package com.orion.mdd.mddapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private AuthService authService;

	/**
	 * @param id
	 * @return ResponseEntity<UserDTO>
	 */
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
		User user = userService.getUserById(id);
		UserDTO userDTO = userMapper.toDto(user);
		return ResponseEntity.ok(userDTO);
	}

	@GetMapping("/{id}/subjects")
	public ResponseEntity<UserWithSubjectsDTO> getSubjectsForCurrentUser(@PathVariable Long id) {
		User user = userService.getUserById(id);

		UserWithSubjectsDTO dto = userMapper.toUserWithSubjectsDTO(user);
		return ResponseEntity.ok(dto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable String id, @RequestBody UserUpdatedDTO userUpdatedDto) {
		try {
			User user = authService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
			if (!user.getId().equals(Long.parseLong(id))) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN)
						.body("You are not authorized to modify this user");
			}
			String token = this.userService.update(user, this.userMapper.toEntity(userUpdatedDto));

			return ResponseEntity.ok().body(new AuthDTO(token));
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
}