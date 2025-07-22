package com.orion.mdd.mddapi.services;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
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

	public List<PostDTO> getPostsForUser(User user) {
		Set<Subject> subscribedSubjects = user.getSubscribedSubjects();
		List<Post> articles = postRepository.findBySubjectIn(subscribedSubjects);
		return postMapper.toDtoList(articles);
	}

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

	public Post findById(Long id) {
		Post post = postRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Post not found"));

		// Forcer le chargement des associations LAZY
		post.getAuthor().getId();
		post.getSubject().getId();

		return post;
	}
}
