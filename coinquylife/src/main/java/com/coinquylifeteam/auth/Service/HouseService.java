package com.coinquylifeteam.auth.Service;

import com.coinquylifeteam.auth.Data.House;
import com.coinquylifeteam.auth.JWT.TokenManager;
import com.coinquylifeteam.auth.Repository.IHouseRepository;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.stereotype.Service;

@Service
public class HouseService {

    @Autowired
    private IHouseRepository houseRepository;

    @Autowired
    private TokenManager tokenManager;

    public Response createHouse(String houseName, String houseAddress) {

        if (houseRepository.findByHouseName(houseName) != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("House with this name already exists")
                    .build();
        }

        // Create a new house object
        House newHouse = new House();
        newHouse.setHouseName(houseName);
        newHouse.setHouseAddress(houseAddress);

        // Generate a unique house code (for example, using UUID)
        String houseCode = java.util.UUID.randomUUID().toString();

        // Check if the generated house code already exists
        while (houseRepository.findByHouseCode(houseCode) != null) {
            houseCode = java.util.UUID.randomUUID().toString();
        }

        // Hash the house code for security
        String hashedHouseCode = BCrypt.hashpw(houseCode, BCrypt.gensalt());

        // Set the hashed house code
        newHouse.setHouseCode(hashedHouseCode);

        // Save the house to the database
        houseRepository.save(newHouse);

        return Response.status(Response.Status.CREATED)
                .entity("{\"code\":\"" + houseCode + "\"}")
                .type("application/json")
                .build();
    }

    public Response loginHouse(String houseCode) {
        House house = houseRepository.findAll()
                .stream()
                .filter(h -> BCrypt.checkpw(houseCode, h.getHouseCode()))
                .findFirst()
                .orElse(null);

        if (house == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Invalid house code")
                    .build();
        }

        return Response.ok("House logged in successfully").build();
    }
}