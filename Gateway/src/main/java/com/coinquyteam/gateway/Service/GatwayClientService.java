package com.coinquyteam.gateway.Service;

import com.coinquyteam.gateway.JWT.TokenManager;
import com.coinquyteam.gateway.Utility.GatewayResult;
import com.coinquyteam.gateway.Utility.StatusGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service responsabile per la generazione e la verifica dei token JWT per i client gateway.
 */
@Service
public class GatwayClientService {

    @Autowired
    private TokenManager tokenManager;

    /**
     * Genera un token JWT per l'utente specificato.
     *
     * @param username il nome utente per cui generare il token
     * @return GatewayResult contenente lo stato e il token generato
     * @throws IllegalArgumentException se lo username è nullo o vuoto
     */
    public GatewayResult generateToken(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        String token = tokenManager.generateToken(username);

        if (token != null) {
            return new GatewayResult(StatusGateway.TOKEN_GENERATION_SUCCESS, token);
        } else {
            return new GatewayResult(StatusGateway.TOKEN_GENERATION_FAILED, null);
        }

    }

    /**
     * Verifica la validità di un token JWT.
     *
     * @param token il token da verificare
     * @return GatewayResult contenente lo stato e il nome utente associato se valido
     * @throws IllegalArgumentException se il token è nullo o vuoto
     */
    public GatewayResult verifyToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }

        String username = tokenManager.verifyToken(token);

        if (username != null) {
            return new GatewayResult(StatusGateway.TOKEN_VERIFICATION_SUCCESS, username);
        } else {
            return new GatewayResult(StatusGateway.TOKEN_VERIFICATION_FAILED, null);
        }
    }

}