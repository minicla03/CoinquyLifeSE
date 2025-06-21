package com.coinquyteam.gateway.JWT;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

import java.util.Collections;

/**
 * Implementazione di ReactiveAuthenticationManager per la gestione dell'autenticazione JWT in modo reattivo.
 * Utilizza un TokenManager per verificare la validità del token JWT e, se valido, restituisce un oggetto Authentication.
 */
public class JWTReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    private final TokenManager tokenManager;

    /**
     * Costruttore che accetta un TokenManager per la gestione dei token JWT.
     * @param tokenManager istanza di TokenManager per la verifica dei token
     */
    public JWTReactiveAuthenticationManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    /**
     * Autentica l'utente in modo reattivo utilizzando il token JWT presente nelle credenziali.
     * Se il token è valido, restituisce un oggetto Authentication, altrimenti un Mono vuoto o un errore.
     * @param authentication oggetto Authentication contenente le credenziali (token JWT)
     * @return Mono contenente Authentication se il token è valido, Mono vuoto o errore altrimenti
     */
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        Object credentials = authentication.getCredentials();
        if (credentials == null) {
            // Nessun token JWT trovato nelle credenziali
            return Mono.error(new RuntimeException("No JWT token found in credentials"));
        }
        String token = credentials.toString();

        // Verifica la validità del token e ottiene lo username associato
        String username = tokenManager.verifyToken(token);
        if (username != null) {
            // Token valido: restituisce un oggetto Authentication con username e token
            return Mono.just(new UsernamePasswordAuthenticationToken(username, token, Collections.emptyList()));
        }
        // Token non valido: restituisce Mono vuoto
        return Mono.empty();
    }
}

/*
 * La classe implementa ReactiveAuthenticationManager di Spring Security,
 * che richiede un metodo authenticate che restituisce un oggetto Mono<Authentication>.
 * Mono è un tipo reattivo di Project Reactor che rappresenta un valore asincrono o nessun valore.
 * Questo approccio permette di gestire l'autenticazione in modo non bloccante e asincrono,
 * ideale per applicazioni reactive come quelle basate su Spring WebFlux.
 * In questo modo, le operazioni di autenticazione possono essere eseguite senza bloccare il thread,
 * migliorando la scalabilità dell'applicazione.
 */