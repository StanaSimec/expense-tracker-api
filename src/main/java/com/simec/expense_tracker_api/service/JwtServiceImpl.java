package com.simec.expense_tracker_api.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class JwtServiceImpl implements JwtService {

    private static final String EMAIL = "email";
    private final Algorithm algorithm;
    private final Duration duration;
    private final String issuer;

    @Autowired
    public JwtServiceImpl(@Value("${jwt.secret}") String secret, @Value("${jwt.duration}") Duration duration,
                          @Value("${jwt.issuer}") String issuer) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.duration = duration;
        this.issuer = issuer;
    }

    @Override
    public String generateForEmail(String email) {
        return JWT.create()
                .withIssuer(issuer)
                .withClaim(EMAIL, email)
                .withExpiresAt(Instant.now().plus(duration))
                .sign(algorithm);
    }

    @Override
    public void validateToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
        verifier.verify(token);
    }

    @Override
    public String extractEmail(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim(EMAIL).asString();
    }
}
