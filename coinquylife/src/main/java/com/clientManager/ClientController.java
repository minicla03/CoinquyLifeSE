package com.clientManager;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;

@Path("/client")
public class ClientController {


    // DA MODIFiCARE
    @GET
    @Path("/house")
    @Produces(MediaType.TEXT_HTML)
    public Response houseRegistrationPage() {
        return Response.seeOther(URI.create("http://localhost:8080/Clients/HousePage.html")).build();
    }
}
