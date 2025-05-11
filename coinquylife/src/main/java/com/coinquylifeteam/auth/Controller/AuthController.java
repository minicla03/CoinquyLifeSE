package com.coinquylifeteam.auth.Controller;

import com.coinquylifeteam.auth.Data.User;
import com.coinquylifeteam.auth.Service.AuthService;
import com.coinquylifeteam.auth.Utility.AuthResult;
import com.coinquylifeteam.auth.Utility.StatusAuth;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;


@Path("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @POST
    @Path("/login")
    @Consumes("application/json")
    public Response login(User user)
    {
        String username = user.getUsername();
        String password = user.getPassword();

        AuthResult result = authService.login(username, password);
        if (result.getStatusAuth()== StatusAuth.SUCCESS)
        {
            return Response.ok("Login successful").build();
        }
        else if(result.getStatusAuth() == StatusAuth.USER_NOT_FOUND)
        {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
        else if (result.getStatusAuth() == StatusAuth.INVALID_CREDENTIALS)
        {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred").build();
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

}
