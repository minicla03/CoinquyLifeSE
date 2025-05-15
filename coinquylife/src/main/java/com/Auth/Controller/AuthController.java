package com.Auth.Controller;

import com.Auth.Data.User;
import com.Auth.Service.AuthService;
import com.Auth.Utility.AuthResult;
import com.Auth.Utility.StatusAuth;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@Path("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @POST
    @Path("/login")
    @Consumes("application/json")
    @Produces("application/json")
    public Response login(User user)
    {
        AuthResult result;
        String password = user.getPassword();
        String username = user.getUsername();
        if (username == null ) {
            String email = user.getEmail();
            result = authService.login(email, password);
        }
        else {
            result = authService.login(username, password);
        }

        if (result.getStatusAuth()== StatusAuth.SUCCESS)
        {
            System.out.println("Token: " + result.getToken());
            // Generate a token and return it in the response
            return Response.ok("{\"token\":\"" + result.getToken() + "\"}").type("application/json").build();
        }
        else if(result.getStatusAuth() == StatusAuth.USER_NOT_FOUND)
        {
            return Response.status(Response.Status.NOT_FOUND).entity("Utente non trovato").build();
        }
        else if (result.getStatusAuth() == StatusAuth.INVALID_CREDENTIALS)
        {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Credenziali errate o non valide").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error").build();
    }

    @POST
    @Path("/register")
    public Response register(User user){

        String username = user.getUsername();
        String password = user.getPassword();
        String name = user.getName();
        String surname = user.getSurname();
        String email = user.getEmail();

        AuthResult result = authService.register(username, name, password, surname, email);
        if (result.getStatusAuth() == StatusAuth.SUCCESS) {
            return Response.ok("Registration successful").build();
        } else if (result.getStatusAuth() == StatusAuth.USER_ALREADY_EXISTS) {
            return Response.status(Response.Status.CONFLICT).entity("User already exists").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred").build();
    }

    @POST
    @Path("/link-house")
    @Consumes("application/json")
    public Response linkHouse(@HeaderParam("Authorization") String authHeader, Map<String, String> body) {

        // Verifica se il token è presente
        String token = authHeader.replace("Bearer ", "");

        // Autentica utente tramite token
        String username = authService.getUsernameFromToken(token);
        String houseCode = body.get("houseCode");

        if (username == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }

        // Associa la casa all’utente (es. salva nel DB)
        Response success = authService.linkHouseToUser(username, houseCode);

        // Verifica se l'utente ha già una casa associata
        if (success.getStatus() == 409) {
            return Response.status(Response.Status.CONFLICT).entity("Failed to link house").build();
        }

        // Restituisci una risposta di successo
        return Response.status(success.getStatus()).entity(success.getEntity()).build();
    }


    @POST
    @Path("/infoUser")
    @Consumes("application/json")
    public Response getUserInfo(@HeaderParam("Authorization") String authHeader, Map<String, String> body)
    {
        // Verifica se il token è presente
        String token = authHeader.replace("Bearer ", "");
        String username = authService.getUsernameFromToken(token);

        if (username == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }

        // Restituisci le informazioni dell'utente
        User user = authService.getUserInfo(username);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }

        return Response.ok(user).build();
    }



}