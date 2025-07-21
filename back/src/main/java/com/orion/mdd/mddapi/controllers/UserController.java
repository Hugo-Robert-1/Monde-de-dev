package com.orion.mdd.mddapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orion.mdd.mddapi.dtos.UserDTO;
import com.orion.mdd.mddapi.dtos.UserWithSubjectsDTO;
import com.orion.mdd.mddapi.mapper.UserMapper;
import com.orion.mdd.mddapi.models.User;
import com.orion.mdd.mddapi.services.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserMapper userMapper;

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
}