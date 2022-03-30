package com.example.springfileuploader.controller;

import com.example.springfileuploader.exception.AuthorizationException;
import com.example.springfileuploader.exception.DuplicateEntryException;
import com.example.springfileuploader.security.JwtUtil;
import com.example.springfileuploader.service.AuthService;
import com.example.springfileuploader.controller.models.JwtDTO;
import com.example.springfileuploader.controller.models.UserPasswordDTO;
import com.example.springfileuploader.repository.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtUtil jwtUtil){
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Registers a user profile and logs in said user.
     * @param user a {@link UserPasswordDTO} containing the credentials to be used to create a new user.
     * @return a token to authenticate the newly created user.
     * @throws DuplicateEntryException if a user with the supplied email already exists.
     */
    @PostMapping("/api/register")
    public ResponseEntity<JwtDTO> registerAndLogin(@Valid @RequestBody UserPasswordDTO user) throws DuplicateEntryException {
        User createdUser = authService.createAndRegisterUser(user).orElseThrow(() ->
                new DuplicateEntryException("User already exists")
        );
        log.info("Successfully created and registered a new user");
        String token = jwtUtil.generateJWT(user.getEmail());
        return ResponseEntity.ok(new JwtDTO(token));
    }

    /**
     * Logs in a user when given valid credentials.
     * @param loginCredentials a {@link UserPasswordDTO} containing the email and password used to log in a user.
     * @return A token to be used for user authentication in requests.
     * @throws AuthorizationException if the supplied credentials are not valid.
     */
    @PostMapping("/api/login")
    public ResponseEntity<JwtDTO> loginHandler(@Valid @RequestBody UserPasswordDTO loginCredentials)
            throws AuthorizationException {
             UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
                     loginCredentials.getEmail(),
                     loginCredentials.getPassword()
             );
        try {
            authenticationManager.authenticate(authInputToken);
            log.info("Successfully authenticated and logged in user");
        } catch(org.springframework.security.core.AuthenticationException e){
            throw new AuthorizationException("Bad credentials");
        }
        String token = jwtUtil.generateJWT(loginCredentials.getEmail());
        return ResponseEntity.ok(new JwtDTO(token));
    }

}
