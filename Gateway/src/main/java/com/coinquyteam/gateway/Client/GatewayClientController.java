package com.coinquyteam.gateway.Client;

import com.coinquyteam.gateway.Service.GatwayClientService;
import com.coinquyteam.gateway.Utility.GatewayResult;
import com.coinquyteam.gateway.Utility.StatusGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/gateway")
public class GatewayClientController {

    @Autowired
    private GatwayClientService gatwayClientService;

    /**
     * Endpoint per generare un token per l'utente specificato.
     * @param body Mappa contenente il campo "username".
     * @return ResponseEntity con il token generato o errore.
     */
    @PostMapping(value = "/generate-token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> generateToken(@RequestBody Map<String, String> body) {
        System.out.println(body);

        GatewayResult result = gatwayClientService.generateToken(body.get("username"));

        if (result.getStatusGateway() == StatusGateway.TOKEN_GENERATION_SUCCESS) {
            return ResponseEntity.ok(Map.of("token", result.getToken()));
        } else {
            return ResponseEntity.status(500).body("Token generation failed");
        }
    }

    /**
     * Endpoint per verificare la validità di un token.
     * @param body Mappa contenente il campo "token".
     * @return ResponseEntity con il nome utente associato o errore.
     */
    @PostMapping(value = "/verify-token", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> verifyToken(@RequestBody Map<String, String> body) {

        GatewayResult result = gatwayClientService.verifyToken(body.get("token"));

        if (result.getStatusGateway() == StatusGateway.TOKEN_VERIFICATION_SUCCESS) {
            return ResponseEntity.ok(Map.of("username", result.getToken()));
        } else {
            return ResponseEntity.status(401).body("Token verification failed");
        }
    }
}