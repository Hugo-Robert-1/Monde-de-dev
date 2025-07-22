package com.orion.mdd.mddapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orion.mdd.mddapi.models.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
