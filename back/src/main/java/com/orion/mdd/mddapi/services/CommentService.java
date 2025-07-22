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

@Service
public class CommentService {
	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

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