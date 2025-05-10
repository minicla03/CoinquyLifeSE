package com.coinquylifeteam.auth.Controller;

import com.coinquylifeteam.auth.Service.AuthService;
import com.coinquylifeteam.auth.Utility.AuthResult;
import com.coinquylifeteam.auth.Utility.StatusAuth;
import jakarta.ws.rs.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;

@Path("/auth")
//@Consumes("application/x-www-form-urlencoded")
public class AuthController {

    @Autowired
    private AuthService authService;

    @POST
    @Path("/login")
    public Response login(@FormParam("username") String username, @FormParam("password") String password)
    {
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

    /*@POST
    @Path("/register")
    public Response register(@FormParam("username") String username, @FormParam("name") String name,
                             @FormParam("password") String password,
                             @FormParam("surname") String surname, @FormParam("email") String email)
    {
        AuthResult result = authService.register(username, name, password, surname, email);
        if (result.getStatusAuth() == StatusAuth.SUCCESS)
        {
            return Response.ok("Registration successful").build();
        }
        else if (result.getStatusAuth() == StatusAuth.USER_ALREADY_EXISTS)
        {
            return Response.status(Response.Status.CONFLICT).entity("User already exists").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred").build();
    }*/

    @POST
    @Path("/register")
    public Response register(@QueryParam("username") String username, @QueryParam("name") String name,
                             @QueryParam("password") String password,
                             @QueryParam("surname") String surname, @QueryParam("email") String email)
    {
        AuthResult result = authService.register(username, name, password, surname, email);
        if (result.getStatusAuth() == StatusAuth.SUCCESS)
        {
            return Response.ok("Registration successful").build();
        }
        else if (result.getStatusAuth() == StatusAuth.USER_ALREADY_EXISTS)
        {
            return Response.status(Response.Status.CONFLICT).entity("User already exists").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("An error occurred").build();
    }
}
