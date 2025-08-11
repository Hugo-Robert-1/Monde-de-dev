package com.orion.mdd.mddapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orion.mdd.mddapi.dtos.CommentCreateDTO;
import com.orion.mdd.mddapi.dtos.CommentDTO;
import com.orion.mdd.mddapi.mapper.CommentMapper;
import com.orion.mdd.mddapi.models.Comment;
import com.orion.mdd.mddapi.models.User;
import com.orion.mdd.mddapi.services.AuthService;
import com.orion.mdd.mddapi.services.CommentService;

import jakarta.validation.Valid;

/**
 * Contrôleur REST gérant les opérations de création de message
 * <p>
 * Ce contrôleur fournit un point d'entrée pour :
 * <ul>
 * <li>Création d'un commentaire en lien avec un article et un utilisateur</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/comment")
public class CommentController {

	@Autowired
	private CommentService commentService;

	@Autowired
	private CommentMapper commentMapper;

	@Autowired
	private AuthService authService;

	/**
	 * Crée un commentaire et le lie à un utilisateur et à un article
	 *
	 * @param commentCreateDTO contenant le message ainsi que l'id de l'article
	 * @return un {@link CommentDTO} contenant les infos relatives au message
	 *         (contenu, utilisateur, date d'écriture)
	 */
	@PostMapping
	public ResponseEntity<CommentDTO> createComment(@RequestBody @Valid CommentCreateDTO commentCreateDTO) {
		User user = authService.findUserByIdentifier(SecurityContextHolder.getContext().getAuthentication().getName());
		Comment saved = commentService.createComment(commentCreateDTO, user);
		return ResponseEntity.ok(commentMapper.toDto(saved));
	}
}
