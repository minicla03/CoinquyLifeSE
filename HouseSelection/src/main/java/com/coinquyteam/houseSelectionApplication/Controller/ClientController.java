package com.coinquyteam.houseSelectionApplication.Controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/client")
public class ClientController
{
    @GET
    @Path("/dash")
    @Produces(MediaType.TEXT_HTML)
    public Response houseRegistrationPage()
    {
        return Response.ok("{\"path\": \"http://localhost:8083/House/dashPage.html\"}", MediaType.APPLICATION_JSON).build();
    }
}