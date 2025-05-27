package com.coiquyteam.apigateway.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenManager
{
    private static final Logger logger = LoggerFactory.getLogger(TokenManager.class);

    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public TokenManager(@Value("${jwt.secret}") String secret)
    {
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).build();
    }

    public boolean verifyToken(String token)
    {
        try {
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token)
    {
        try {
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (Exception e) {
            logger.warn("Token extraction failed: {}", e.getMessage());
            return null;
        }
    }

}
