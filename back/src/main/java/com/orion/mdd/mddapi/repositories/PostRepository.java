package com.orion.mdd.mddapi.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.orion.mdd.mddapi.models.Post;
import com.orion.mdd.mddapi.models.Subject;

public interface PostRepository extends JpaRepository<Post, Long> {
	List<Post> findBySubjectIn(Set<Subject> subjects);

	List<Post> findBySubjectIdIn(List<Long> subjectIds, Sort sort);
}
