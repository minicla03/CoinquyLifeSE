package com.coinquyteam.houseSelectionApplication.Controller;

import com.coinquyteam.houseSelectionApplication.Data.House;
import com.coinquyteam.houseSelectionApplication.Service.HouseService;
import com.coinquyteam.houseSelectionApplication.Utility.HouseResult;
import com.coinquyteam.houseSelectionApplication.Utility.HouseStatus;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

// Controller per la gestione delle case
@Controller
@Path("/house")
public class HouseController {

    @Autowired
    private HouseService houseService;

    // Endpoint per la creazione di una nuova casa
    @POST
    @Path("/create")
    public Response createHouse(@HeaderParam("Authorization") String auth, House house)
    {
        // Estrae nome e indirizzo della casa dall'oggetto House
        String houseName = house.getHouseName();
        String houseAddress = house.getHouseAddress();

        // Tenta di creare la casa tramite il service
        HouseResult houseResult = houseService.createHouse(houseName, houseAddress);

        // Estrae il token JWT dall'header Authorization
        String token = auth.substring(7);
        System.out.println(token);

        System.out.println();

        // Verifica lo stato della creazione della casa
        if(houseResult.getHouseStatus() == HouseStatus.HOUSE_CREATED)
        {
            // Collega la casa appena creata all'utente tramite il token
            HouseResult houseResult1 = houseService.linkHouseToUser(token, houseResult.getMessage());

            if (houseResult1.getHouseStatus() == HouseStatus.LINKED_SUCCES)
            {
                // Casa creata e collegata con successo
                return Response.ok("{\"code\":\"" + houseResult.getMessage() + "\"}", "application/json").build();
            }
            else if(houseResult1.getHouseStatus() == HouseStatus.USER_NOT_FOUND)
            {
                // Utente non trovato
                return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
            }
            else if(houseResult1.getHouseStatus() == HouseStatus.USER_ALREADY_LINKED)
            {
                // L'utente è già collegato a una casa, elimina la casa appena creata
                HouseResult houseResult3 = houseService.deleteHouse(houseResult.getMessage());
                if (houseResult3.getHouseStatus() == HouseStatus.HOUSE_DELETED)
                {
                    return Response.status(Response.Status.CONFLICT).entity("User already linked to a house").build();
                }
                else
                {
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error deleting house").build();
                }
            }
            else
            {
                // Errore generico nel collegamento casa-utente
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error linking house to user").build();
            }

        }
        else if(houseResult.getHouseStatus() == HouseStatus.HOUSE_ALREADY_EXISTS)
        {
            // La casa esiste già
            return Response.status(Response.Status.CONFLICT).entity("La casa con questo nome esiste già").build();
        }
        else if(houseResult.getHouseStatus() == HouseStatus.HOUSE_NOT_CREATED)
        {
            // Errore nella creazione della casa
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore: " + houseResult.getMessage()).build();
        }
        else
        {
            // Errore generico del server
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error").build();
        }
    }

    // Endpoint per il login in una casa tramite codice
    @POST
    @Path("/loginHouse")
    public Response loginHouse(@HeaderParam("Authorization") String auth, House house) {
        // Estrae il codice della casa dall'oggetto House
        String houseCode = house.getHouseId();
        // Estrae il token JWT dall'header Authorization
        String token = auth.substring(7);

        // Tenta di trovare la casa tramite il service
        HouseResult houseResult = houseService.loginHouse(houseCode);

        if (houseResult.getHouseStatus() == HouseStatus.HOUSE_FOUND)
        {
            // Collega la casa all'utente
            HouseResult houseResult1 = houseService.linkHouseToUser(token, houseCode);
            if (houseResult1.getHouseStatus() == HouseStatus.LINKED_SUCCES)
            {
                // Casa collegata con successo
                return Response.ok("{\"message\":\"House linked successfully\"}", "application/json").build();
            }
            else if(houseResult1.getHouseStatus() == HouseStatus.USER_NOT_FOUND)
            {
                // Utente non trovato
                return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
            }
            else if(houseResult1.getHouseStatus() == HouseStatus.USER_ALREADY_LINKED)
            {
                // Utente già collegato a una casa
                return Response.status(Response.Status.CONFLICT).entity("User already linked to a house").build();
            }
            else
            {
                // Errore generico nel collegamento casa-utente
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error linking house to user").build();
            }
        }
        else if (houseResult.getHouseStatus() == HouseStatus.HOUSE_NOT_FOUND)
        {
            // Casa non trovata
            return Response.status(Response.Status.NOT_FOUND).entity("House not found").build();
        }
        else
        {
            // Errore generico del server
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error").build();
        }
    }

}