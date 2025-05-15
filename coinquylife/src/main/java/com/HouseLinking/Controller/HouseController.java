package com.HouseLinking.Controller;

import com.HouseLinking.Data.House;
import com.HouseLinking.Service.HouseService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

import java.util.Map;

@Path("/house")
public class HouseController {

    @Autowired
    private HouseService houseService;

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
        Response creationResponse = houseService.loginHouse(houseCode); // esempio

        if (creationResponse.getStatus() != 404) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setBearerAuth(auth.replace("Bearer ", ""));

                Map<String, String> body = Map.of("houseCode", houseCode);
                HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

                ResponseEntity<String> response = new RestTemplate()
                        .postForEntity("http://localhost:8081/rest/auth/linkHouse", request, String.class);

                return Response.status(response.getStatusCodeValue()).entity(response.getBody()).build();

            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Errore durante la comunicazione con AuthService: " + e.getMessage())
                        .build();
            }
        }
        // Se la casa non esiste, restituisci una risposta 404
        if (creationResponse.getStatus() == 404) {
            return Response.status(Response.Status.NOT_FOUND).entity("La casa non esiste").build();
        }

        return Response.status(creationResponse.getStatus()).entity(creationResponse).build();
    }

}
