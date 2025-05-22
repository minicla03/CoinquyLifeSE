package com.coinquyteam.authApplication.Controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("/client")
public class ClientController
{
    @GET
    @Path("/house")
    @Produces(MediaType.TEXT_HTML)
    public Response houseRegistrationPage() {
        return Response.seeOther(URI.create("http://localhost:8081/HousePage.html")).build();
    }
}