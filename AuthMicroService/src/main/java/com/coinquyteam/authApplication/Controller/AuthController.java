package com.coinquyteam.authApplication.Controller;

import com.coinquyteam.authApplication.Data.User;
import com.coinquyteam.authApplication.Service.AuthService;
import com.coinquyteam.authApplication.Utility.AuthResult;
import com.coinquyteam.authApplication.Utility.StatusAuth;
import com.coinquyteam.authApplication.Utility.UserResult;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


// Controller per la gestione delle operazioni di autenticazione
@Path("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Espressione regolare per la validazione dell'email
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // Endpoint per il login dell'utente
    @POST
    @Path("/login")
    @Consumes("application/json")
    @Produces("application/json")
    public Response login(User user) {
        AuthResult result;
        String password = user.getPassword();
        String username = user.getUsername();
        if (username == null) {
            String email = user.getEmail();
            result = authService.login(email, password);
        } else {
            result = authService.login(username, password);
        }

        if (result.getStatusAuth() == StatusAuth.SUCCESS) {
            // Restituisce il token in caso di successo
            return Response.ok("{\"token\":\"" + result.getToken() + "\"}").type("application/json").build();
        } else if (result.getStatusAuth() == StatusAuth.USER_NOT_FOUND) {
            // Utente non trovato
            return Response.status(Response.Status.NOT_FOUND).entity("Utente non trovato").build();
        } else if (result.getStatusAuth() == StatusAuth.INVALID_CREDENTIALS) {
            // Credenziali errate
            return Response.status(Response.Status.UNAUTHORIZED).entity("Credenziali errate o non valide").build();
        }
        // Errore generico del server
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error").build();
    }

    // Endpoint per la registrazione di un nuovo utente
    @POST
    @Path("/register")
    public Response register(User user) {

        String username = user.getUsername();
        String password = user.getPassword();
        String name = user.getName();
        String surname = user.getSurname();
        String email = user.getEmail();

        // Validazione dell'email
        if (!validateEmail(email)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Email invalido").build();
        }

        AuthResult result = authService.register(username, name, password, surname, email);
        if (result.getStatusAuth() == StatusAuth.SUCCESS) {
            // Registrazione avvenuta con successo
            return Response.ok("Registration successful").build();
        } else if (result.getStatusAuth() == StatusAuth.USER_ALREADY_EXISTS) {
            // Utente già esistente
            return Response.status(Response.Status.CONFLICT).entity("User already exists").build();
        }
        // Errore generico
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred").build();
    }

    // Metodo di utilità per validare l'email
    private static boolean validateEmail(String email) {
        if (email == null) return false;
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }

    // Endpoint per ottenere gli utenti associati a un determinato houseId
    @POST
    @Path("/getUserByHouseId")
    @Consumes("application/json")
    @Produces("application/json")
    public Response getUserByHouseId(Map<String, String> requestBody) {
        String houseId = requestBody.get("houseId");
        UserResult result = authService.getUserByHouseId(houseId);
        if (result.getStatusAuth() == StatusAuth.USERS_FOUNDED) {
            // Utenti trovati
            return Response.ok(result.getUsers()).build();
        } else if (result.getStatusAuth() == StatusAuth.USERS_NOT_FOUND) {
            // Nessun utente trovato per l'houseId fornito
            return Response.status(Response.Status.NOT_FOUND).entity("No users found for the given house ID").build();
        }
        // Nessuna risposta (potrebbe essere migliorato)
        return null;
    }
}