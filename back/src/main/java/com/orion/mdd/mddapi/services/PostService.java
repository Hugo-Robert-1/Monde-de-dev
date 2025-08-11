package com.orion.mdd.mddapi.services;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.orion.mdd.mddapi.dtos.PostCreateDTO;
import com.orion.mdd.mddapi.dtos.PostDTO;
import com.orion.mdd.mddapi.mapper.PostMapper;
import com.orion.mdd.mddapi.models.Post;
import com.orion.mdd.mddapi.models.Subject;
import com.orion.mdd.mddapi.models.User;
import com.orion.mdd.mddapi.repositories.PostRepository;
import com.orion.mdd.mddapi.repositories.SubjectRepository;
import com.orion.mdd.mddapi.repositories.UserRepository;

/**
 * Service pour la gestion des posts/articles.
 * <p>
 * Permet la récupération des posts selon les abonnements de l'utilisateur, la
 * création de posts, et la récupération d'un post précis avec chargement des
 * relations.
 * </p>
 */
@Service
public class PostService {
	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private PostMapper postMapper;

	/**
	 * Récupère la liste des posts correspondant aux sujets auxquels un utilisateur
	 * est abonné.
	 *
	 * @param user utilisateur concerné
	 * @return liste des posts sous forme de DTO
	 */
	public List<PostDTO> getPostsForUser(User user) {
		Set<Subject> subscribedSubjects = user.getSubscribedSubjects();
		List<Post> articles = postRepository.findBySubjectIn(subscribedSubjects);
		return postMapper.toDtoList(articles);
	}

	/**
	 * Récupère les posts pour un utilisateur donné avec un ordre (asc ou desc) sur
	 * la date de création.
	 *
	 * @param userId id de l'utilisateur
	 * @param order  ordre de tri (asc/desc)
	 * @return liste des posts triée sous forme de DTO
	 */
	public List<PostDTO> getPostsFromSubscribedSubjects(Long userId, String order) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));

		Set<Subject> subscribedSubjects = user.getSubscribedSubjects();

		if (subscribedSubjects == null || subscribedSubjects.isEmpty()) {
			return List.of(); // retourne une liste vide si aucun abonnement
		}

		List<Long> subjectIds = subscribedSubjects.stream()
				.map(Subject::getId)
				.toList();

		Sort sort = "asc".equalsIgnoreCase(order)
				? Sort.by("createdAt").ascending()
				: Sort.by("createdAt").descending();

		List<Post> posts = postRepository.findBySubjectIdIn(subjectIds, sort);

		return postMapper.toDtoList(posts);
	}

	/**
	 * Crée un nouveau post à partir des données du DTO et de l'utilisateur auteur.
	 *
	 * @param postDto données du post à créer
	 * @param user    utilisateur auteur
	 */
	public void create(PostCreateDTO postDto, User user) {
		Subject subject = subjectRepository.findById(postDto.subjectId())
				.orElseThrow(() -> new RuntimeException("Subject not found"));

		Post post = new Post();
		post.setTitle(postDto.title());
		post.setContent(postDto.content());
		post.setSubject(subject);
		post.setAuthor(user);

		postRepository.save(post);
	}

	/**
	 * Recherche un post par son identifiant et charge ses relations LAZY (auteur et
	 * sujet).
	 *
	 * @param id identifiant du post
	 * @return post trouvé
	 */
	public Post findById(Long id) {
		Post post = postRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Post not found"));

		// Forcer le chargement des associations LAZY
		post.getAuthor().getId();
		post.getSubject().getId();

		return post;
	}
}
