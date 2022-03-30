package com.example.springfileuploader.service;

import com.example.springfileuploader.controller.models.UserPasswordDTO;
import com.example.springfileuploader.repository.models.User;
import com.example.springfileuploader.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.springfileuploader.service.enums.Role.ROLE_USER;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new user and saves it to a user collection.
     * @param userCredentials a {@link UserPasswordDTO} containing the email and password of the user to be created.
     * @return an {@link Optional} containing the newly created {@link User} if successful.
     * {@code Optional.empty} if the email already exists in the collection.
     */
    public Optional<User> createAndRegisterUser(UserPasswordDTO userCredentials) {
        try {
            userCredentials.setPassword(passwordEncoder.encode(userCredentials.getPassword()));
            User newUser = userRepository.save(new User(userCredentials, List.of(ROLE_USER.toString())));
            return Optional.of(newUser);
        } catch(DuplicateKeyException e){
            return Optional.empty();
        }
    }
}
