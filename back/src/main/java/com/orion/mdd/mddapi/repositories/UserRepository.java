package com.orion.mdd.mddapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orion.mdd.mddapi.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}

