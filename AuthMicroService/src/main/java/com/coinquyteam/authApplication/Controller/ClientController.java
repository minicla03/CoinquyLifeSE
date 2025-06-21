package com.coinquyteam.authApplication.Controller;

import com.coinquyteam.authApplication.Data.User;
import com.coinquyteam.authApplication.Service.AuthService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Path("/client")
public class ClientController
{
    // Inietta il servizio AuthService per gestire le operazioni e il recupero dei dati
    @Autowired private AuthService authService;

    // Endpoint GET per la pagina di registrazione della casa
    @GET
    @Path("/house")
    @Produces(MediaType.TEXT_HTML)
    public Response houseRegistrationPage() {
        // Restituisce il path della pagina HousePage.html come JSON
        return Response.ok("{\"path\": \"http://localhost:8080/HousePage.html\"}", MediaType.APPLICATION_JSON).build();
    }

    // Endpoint GET per recuperare i coinquilini associati a una casa
    @GET
    @Path("/retrieveCoinquy")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveCoinquy(@QueryParam("houseId") String houseId)
    {
        // Controlla se il parametro houseId è presente
        if (houseId == null)
        {
            // Restituisce errore 400 se manca il parametro
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Missing houseId parameter\"}")
                    .build();
        }

        // Recupera la lista degli utenti associati alla casa tramite il servizio
        List<User> coinquilini = authService.getCoinquilinibyHouseId(houseId);
        // Restituisce la lista come risposta JSON
        return Response.status(Response.Status.OK).entity(coinquilini).build();
    }
}