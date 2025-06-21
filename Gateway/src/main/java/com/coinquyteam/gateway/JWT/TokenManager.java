package com.coinquyteam.gateway.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class TokenManager {

    // Chiave segreta per la firma dei token, configurata in application.properties
    @Value("${jwt.secret}")
    private String secretKey;

    // Tempo di scadenza del token in millisecondi, configurato in application.properties
    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    /**
     * Genera un JWT per l'utente specificato.
     * @param username nome utente per cui generare il token
     * @return token JWT come stringa
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * Verifica la validità di un token JWT e restituisce il nome utente se valido.
     * @param token il token JWT da verificare
     * @return il nome utente se il token è valido, altrimenti null
     */
    public String verifyToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            // Token non valido o scaduto
            return null;
        }
    }
}