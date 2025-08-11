package com.orion.mdd.mddapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orion.mdd.mddapi.dtos.CommentCreateDTO;
import com.orion.mdd.mddapi.models.Comment;
import com.orion.mdd.mddapi.models.Post;
import com.orion.mdd.mddapi.models.User;
import com.orion.mdd.mddapi.repositories.CommentRepository;
import com.orion.mdd.mddapi.repositories.PostRepository;
import com.orion.mdd.mddapi.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

/**
 * Service gérant la création des commentaires associés aux posts.
 * <p>
 * Permet de créer un commentaire lié à un post et un utilisateur donné.
 * </p>
 */
@Service
public class CommentService {
	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	/**
	 * Crée et persiste un nouveau commentaire basé sur un CommentCreateDTO et un
	 * utilisateur.
	 *
	 * @param dto  les données de création du commentaire (contenu + postId)
	 * @param user l'utilisateur auteur du commentaire
	 * @return le commentaire créé et sauvegardé
	 * @throws EntityNotFoundException si le post référencé n'existe pas
	 */
	public Comment createComment(CommentCreateDTO dto, User user) {
		Post post = postRepository.findById(dto.postId())
				.orElseThrow(() -> new EntityNotFoundException("Post not found"));

		Comment comment = new Comment();
		comment.setContent(dto.content());
		comment.setUser(user);
		comment.setPost(post);

		return commentRepository.save(comment);
	}
}