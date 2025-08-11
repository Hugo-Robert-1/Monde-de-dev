package com.orion.mdd.mddapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.orion.mdd.mddapi.dtos.PostCreateDTO;
import com.orion.mdd.mddapi.dtos.PostDTO;
import com.orion.mdd.mddapi.dtos.PostWithCommentsDTO;
import com.orion.mdd.mddapi.mapper.CommentMapper;
import com.orion.mdd.mddapi.mapper.PostMapper;
import com.orion.mdd.mddapi.models.Post;
import com.orion.mdd.mddapi.models.User;
import com.orion.mdd.mddapi.services.AuthService;
import com.orion.mdd.mddapi.services.PostService;

/**
 * Contrôleur REST gérant les opérations de gestion des Articles
 * <p>
 * Ce contrôleur fournit un point d'entrée pour :
 * <ul>
 * <li>Création d'un article</li>
 * <li>Récupération d'un article grâce à son ID</li>
 * <li>Récupération de tous les articles qui concernent les thèmes auxquels
 * l'utilisateur est abonné</li>
 * </ul>
 * </p>
 */
@RestController
@RequestMapping("/api/post")
public class PostController {
	@Autowired
	private PostMapper postMapper;

	@Autowired
	private PostService postService;

	@Autowired
	private AuthService authService;

	@Autowired
	private CommentMapper commentMapper;

	/**
	 * Crée un article et le lie à un thème et à un autheur.
	 *
	 * @param postDto contient le titre, le contenu de l'article et l'id du thème
	 * @return une reponse http code 200
	 * @throws UserNotFoundException si l'utilisateur authentifié n'existe pas
	 * @throws RuntimeException      si le thème associé à l'article n'est pas
	 *                               trouvé
	 */
	@PostMapping
	public ResponseEntity<?> create(@RequestBody PostCreateDTO postDto) {
		User user = authService.findUserByIdentifier(SecurityContextHolder.getContext().getAuthentication().getName());

		this.postService.create(postDto, user);
		return ResponseEntity.ok().build();
	}

	/**
	 * Récupère un article via son id.
	 *
	 * @param id identifiant de l'article
	 * @return un {@link PostWithCommentsDTO} contenant toutes les infos de
	 *         l'article ainsi que les commentaires qui y sont liés
	 * @throws RuntimeException si l'article associé à l'id n'est pas trouvé
	 */
	@GetMapping("/{id}")
	public ResponseEntity<PostWithCommentsDTO> getPostById(@PathVariable Long id) {
		Post post = postService.findById(id);
		PostWithCommentsDTO dto = postMapper.toDtoWithComments(post, commentMapper);
		return ResponseEntity.ok(dto);
	}

	/**
	 * Récuère tous les articles qui ont un thème pour lequel l'utilisateur est
	 * abonné
	 * 
	 * @param order pour faire un tri ascendant/descendant
	 * @return une liste de {@link PostDTO}
	 */
	@GetMapping("/subscribed")
	public List<PostDTO> getPostsFromSubscribedSubjects(
			@RequestParam(defaultValue = "desc") String order) {
		User user = authService.findUserByIdentifier(SecurityContextHolder.getContext().getAuthentication().getName());

		return postService.getPostsFromSubscribedSubjects(user.getId(), order);
	}

}
