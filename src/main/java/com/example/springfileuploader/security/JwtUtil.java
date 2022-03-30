package com.example.springfileuploader.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt_secret")
    private String secret;
    private final String issuer = "FILE_UPLOADER";
    private final String subject = "USER_DETAILS";

    /**
     * Generates a new Json Web Token signed with the application secret
     * and expiration 3 hours from when it was created.
     * @param email the email used as user credentials.
     * @return the newly generated JWT.
     */
    public String generateJWT(String email) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 3);
        return JWT.create()
                .withSubject(subject)
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withIssuer(issuer)
                .withExpiresAt(calendar.getTime())
                .sign(Algorithm.HMAC256(secret));
    }

    /**
     * Validates that a token has the correct subject and issuer, decodes it,
     * and returns the email used as user credentials.
     * @param token the encoded Json Web Token to be validated.
     * @return the email from the received token.
     * @throws JWTVerificationException if validation of token fails.
     */
    public String validateJWTAndRetrieveUser(String token){
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject(subject)
                .withIssuer(issuer)
                .build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getClaim("email").asString();
    }
}
