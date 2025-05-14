package com.coinquylifeteam.auth.Controller;

import com.coinquylifeteam.auth.Data.House;
import com.coinquylifeteam.auth.Service.HouseService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/house")
public class HouseController {

    @Autowired
    private HouseService houseService;

    @POST
    @Path("/create")
    public Response createHouse(House house) {

        String houseName = house.getHouseName();
        String houseAddress = house.getHouseAddress();

        return houseService.createHouse(houseName, houseAddress);
    }

    @POST
    @Path("/loginHouse")
    public Response loginHouse(@HeaderParam("Authorization") String authHeader, House house) {

        String houseCode = house.getHouseCode();
        return houseService.loginHouse(authHeader, houseCode);
    }
}
