package com.orion.mdd.mddapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orion.mdd.mddapi.dtos.UserDTO;
import com.orion.mdd.mddapi.services.DtoMapperService;
import com.orion.mdd.mddapi.services.UserService;
import com.orion.mdd.mddapi.models.User;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
    private UserService userService;

    @Autowired
    private DtoMapperService dtoMapperService;
	
	/**
     * @param id
     * @return ResponseEntity<UserDTO>
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
    	User user = userService.getUserById(id);
        UserDTO userDTO = dtoMapperService.toUserDTO(user);
        return ResponseEntity.ok(userDTO);
    }
}