package com.orion.mdd.mddapi.services;

import org.springframework.stereotype.Service;

import com.orion.mdd.mddapi.models.User;
import com.orion.mdd.mddapi.repositories.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
    * Get a user by his ID
    * 
    * @param id The ID of the user to retrieve
    * @return User The user corresponding to the ID
    */
    public User getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID : " + id));
        return user;
    }
}
