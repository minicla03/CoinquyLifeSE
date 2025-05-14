package com.coinquylifeteam.auth.Service;

import com.coinquylifeteam.auth.Data.House;
import com.coinquylifeteam.auth.JWT.TokenManager;
import com.coinquylifeteam.auth.Repository.IHouseRepository;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HouseService {

    @Autowired
    private IHouseRepository houseRepository;

    @Autowired
    private TokenManager tokenManager;

    public Response createHouse(String houseName, String houseAddress) {

        if(houseRepository.findByHouseName(houseName) != null) {
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

        // Set the house code
        newHouse.setHouseCode(houseCode);

        // Save the house to the database
        houseRepository.save(newHouse);

        return Response.ok("House created successfully, your invitation code is " + houseCode).build();
    }

    public Response loginHouse(String authHeader, String houseCode) {
        // Verifica presenza del token
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Token mancante o non valido")
                    .build();
        }
        String token = authHeader.substring("Bearer ".length());

        // Qui dovresti validare il token (es. JWT, database, ecc.)
        String username = tokenManager.verifyToken(token);
        if (username == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Token non valido o scaduto")
                    .build();
        }

        House house = houseRepository.findByHouseCode(houseCode);
        if (house == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("House not found")
                    .build();
        }

        return Response.ok("House logged in successfully").build();
    }
}
