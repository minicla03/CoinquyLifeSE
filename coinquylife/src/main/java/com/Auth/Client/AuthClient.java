package com.Auth.Client;
import jakarta.ws.rs.core.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


/**
 * Classe per comunicare con il servizio Auth
 */
@Component
public class AuthClient {

    private final RestTemplate restTemplate;

    public AuthClient() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Metodo per notificare il servizio Auth che una casa è stata creata e associarla all'utente
     * @param token token dell'utente
     * @param houseCode codice della casa
     * @return Response con esito dell'operazione
     */
    public Response linkHouseToUser(String token, String houseCode) {
        String url = "http://localhost:8080/rest/auth/link-house"; // endpoint da creare in Auth

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("houseCode", houseCode);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity res = restTemplate.postForEntity(url, request, String.class);
            if (res.getStatusCode().is2xxSuccessful()) {
                // Gestisci risposta positiva
                System.out.println("House linked successfully");
                return Response.ok().build();
            }
        } catch (Exception e) {
            // Gestisci errore
            System.out.println("Errore nella comunicazione con Auth: " + e.getMessage());
            if(e.getMessage().contains("404")) {
                return Response.status(Response.Status.NOT_FOUND).entity("House not found").build();
            } else if (e.getMessage().contains("401")) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Unauthorized").build();
            } else if (e.getMessage().contains("403")) {
                return Response.status(Response.Status.FORBIDDEN).entity("Forbidden").build();
            } else if (e.getMessage().contains("409")) {
                return Response.status(Response.Status.CONFLICT).entity("Utente già associato ad una casa").build();
            }
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}

