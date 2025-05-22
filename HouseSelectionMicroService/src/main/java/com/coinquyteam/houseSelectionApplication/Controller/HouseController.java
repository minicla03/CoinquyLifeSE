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

@Controller
@Path("/house")
public class HouseController {

    @Autowired
    private HouseService houseService;

    @POST
    @Path("/create")
    public Response createHouse(@HeaderParam("Authorization") String auth, House house)
    {
        String houseName = house.getHouseName();
        String houseAddress = house.getHouseAddress();
        HouseResult houseResult=houseService.createHouse(houseName, houseAddress);
        String token = auth.substring(7);

        if(houseResult.getHouseStatus()== HouseStatus.HOUSE_CREATED)
        {
            HouseResult houseResult1= houseService.linkHouseToUser(token, house.getHouseAddress());

            if (houseResult1.getHouseStatus() == HouseStatus.LINKED_SUCCES)
            {
                return Response.ok("{\"message\":\"House created and linked successfully\"}").build();
            }
            else if(houseResult1.getHouseStatus() == HouseStatus.USER_NOT_FOUND)
            {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
            }
            else
            {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error linking house to user").build();
            }
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

        if (houseResult.getHouseStatus() == HouseStatus.HOUSE_LOGGED_IN)
        {
            return Response.ok("{\"message\":\"House logged in successfully\"}").build();
        }
        else if (houseResult.getHouseStatus() == HouseStatus.HOUSE_NOT_FOUND)
        {
            return Response.status(Response.Status.NOT_FOUND).entity("House not found").build();
        }
        else
        {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error").build();
        }
    }

}
