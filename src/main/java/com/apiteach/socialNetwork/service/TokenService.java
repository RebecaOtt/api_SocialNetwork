package com.apiteach.socialNetwork.service;

import com.apiteach.socialNetwork.dto.res.LoginResDTO;
import com.apiteach.socialNetwork.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${SECRET}")
    private String secret;

    public LoginResDTO generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        Instant expirationDate = getExpirationDate();

        String token = JWT.create()
                .withIssuer("3035teach/apiSocialNetwork")
                .withSubject(user.getUsername())
                .withExpiresAt(expirationDate)
                .sign(algorithm);

        return new LoginResDTO(
                "Bearer",
                token,
                expirationDate.toEpochMilli()
        );

    }

    private Instant getExpirationDate() {
        return LocalDateTime.now()
                .plusHours(3)
                .toInstant(ZoneOffset.of("-03:00"));
    }

    public String ValidateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        String subject = JWT.require(algorithm)
                .withIssuer("3035teach/apiSocialNetwork")
                .build()
                .verify(token)
                .getSubject();

        return subject;
    }
}
