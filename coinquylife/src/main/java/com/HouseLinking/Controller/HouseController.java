package com.HouseLinking.Controller;

import com.Auth.Client.AuthClient;
import com.HouseLinking.Data.House;
import com.HouseLinking.Service.HouseService;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/house")
public class HouseController {

    @Autowired
    private HouseService houseService;

    @Autowired
    private AuthClient authClient; // client per chiamare Auth


    /**
     * Crea una nuova casa con il nome e l'indirizzo forniti.
     *
     * @param house L'oggetto casa che contiene il nome e l'indirizzo della casa.
     * @return Una risposta che indica l'esito dell'operazione di creazione.
     */
    @POST
    @Path("/create")
    public Response createHouse(House house) {
        String houseName = house.getHouseName();
        String houseAddress = house.getHouseAddress();
        return houseService.createHouse(houseName, houseAddress);
    }


    /**
     * Effettua il login di una casa utilizzando il codice fornito.
     *
     * @param auth Il token di autorizzazione.
     * @param house L'oggetto casa che contiene il codice della casa.
     * @return Una risposta che indica l'esito dell'operazione di login.
     */
    @POST
    @Path("/loginHouse")
    public Response loginHouse(@HeaderParam("Authorization") String auth, House house) {
        String houseCode = house.getHouseId();
        System.out.println(houseCode);
        Response creationResponse = houseService.loginHouse(houseCode); // esempio

        if (creationResponse.getStatus() != 404) {
            // Notifica Auth
            String userToken = auth.replace("Bearer ", "");
            return authClient.linkHouseToUser(userToken, houseCode);
        }
        // Se la casa non esiste, restituisci una risposta 404
        if (creationResponse.getStatus() == 404) {
            return Response.status(Response.Status.NOT_FOUND).entity("La casa non esiste").build();
        }

        return Response.status(creationResponse.getStatus()).entity(creationResponse).build();
    }
}
