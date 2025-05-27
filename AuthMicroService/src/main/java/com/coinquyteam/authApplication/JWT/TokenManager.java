package com.coinquyteam.authApplication.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenManager
{
    private final Algorithm algorithm;
    private final long expiration;

    public TokenManager(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expirationMillis)
    {
        this.algorithm = Algorithm.HMAC256(secret);
        this.expiration = expirationMillis;
    }

    public String generateToken(String username)
    {
        return JWT.create()
                .withSubject(username)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))
                .sign(algorithm);
    }

    public String verifyToken(String token)
    {
        try
        {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();  // ritorna lo username
        }
        catch (Exception e)
        {
            System.err.println("Token invalido: " + e.getMessage());
            return null;
        }
    }

    public Date getTokenExpiration(String token)
    {
        try
        {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getExpiresAt();
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
