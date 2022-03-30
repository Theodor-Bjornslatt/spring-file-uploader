package com.example.springfileuploader.repository;

import com.example.springfileuploader.repository.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String>, UserFileRepository {
    /**
     * Performs a case-insensitive search for a username in the database.
     * @param email The email to look for.
     * @return {@link Optional} of a {@link User} if the username is found.
     * {@code Optional.empty} if the username does not exist.
     */
    @Query("{'email':{'$regex':'?0','$options':'i'}}")
    Optional<User> findByEmail(String email);
}
