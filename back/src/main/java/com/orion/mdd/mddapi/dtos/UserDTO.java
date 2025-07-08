package com.orion.mdd.mddapi.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	private Long id;
	private String username;
	private String email;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
}
