package com.coinquyteam.expense.Controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/client")
public class ClientController
{
    //NON USATO
    @GET
    @Path("/backToHome")
    @Produces(MediaType.TEXT_HTML)
    public Response redirectExpense()
    {
        return Response.ok("{\"path\": \"http://localhost:8083/Dashboard/dashPage.html\"}", MediaType.APPLICATION_JSON).build();
    }

}