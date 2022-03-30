package com.example.springfileuploader.security;

import com.example.springfileuploader.repository.models.User;
import com.example.springfileuploader.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     * Loads a user by their email for the DaoAuthenticationProvider.
     * @param email the email of the user to be found.
     * @return an instance of the {@link UserDetailsImpl} containing the email and password of the user.
     * @throws UsernameNotFoundException if no user with the given email exists.
     */
    @Override
    public UserDetailsImpl loadUserByUsername(String email) throws UsernameNotFoundException{
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("Could not find user with email=" + email)
        );
        return new UserDetailsImpl(user);
    }
}
