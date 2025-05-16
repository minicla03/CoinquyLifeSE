package HouseLinking.Controller;

import HouseLinking.Data.House;
import HouseLinking.Service.HouseService;
import HouseLinking.Utility.HouseResult;
import HouseLinking.Utility.HouseStatus;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Controller
@Path("/house")
public class HouseController {

    @Autowired
    private HouseService houseService;
    @Autowired
    private RestTemplate restTemplate;

    @POST
    @Path("/create")
    public Response createHouse(House house)
    {
        String houseName = house.getHouseName();
        String houseAddress = house.getHouseAddress();
        HouseResult houseResult=houseService.createHouse(houseName, houseAddress);

        if(houseResult.getHouseStatus()== HouseStatus.HOUSE_CREATED)
        {
            String url = "http://localhost:8080/auth/external/link-house";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Map<String, String> body = Map.of(); //DA QUIII
            HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode().is2xxSuccessful())
            {

            }
            else
            {

            }
            return Response.status(Response.Status.CREATED).entity("{\"houses\":\"" + houseResult.getMessage() + "\"}").type("application/json").build();
        }
        else if(houseResult.getHouseStatus()== HouseStatus.HOUSE_ALREADY_EXISTS)
        {
            return Response.status(Response.Status.CONFLICT).entity("La casa con questo nome esiste già").build();
        }
        else if(houseResult.getHouseStatus()== HouseStatus.HOUSE_NOT_CREATED)
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore: " + houseResult.getMessage()).build();
        }
        else
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error").build();
        }
    }

    @POST
    @Path("/loginHouse")
    public Response loginHouse(@HeaderParam("Authorization") String auth, House house) {
        String houseCode = house.getHouseId();
        HouseResult houseResult = houseService.loginHouse(houseCode);


        if (houseResult.getStatus() != 404)
        {
            houseService.linkHouseToUser(auth, houseCode);
        }
        else if (houseResult.getStatus() == 404)
        {
            return Response.status(Response.Status.NOT_FOUND).entity("La casa non esiste").build();
        }
        return Response.status(houseResult.getStatus()).entity(houseResult).build();
    }

}
