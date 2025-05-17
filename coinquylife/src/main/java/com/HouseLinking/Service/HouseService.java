package com.HouseLinking.Service;

import com.HouseLinking.Data.House;
import com.HouseLinking.Repository.IHouseRepository;
import jakarta.ws.rs.core.Response;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("HouseService")
public class HouseService {

    @Autowired
    private IHouseRepository houseRepository;


    /**
     * Create a new house with the given name and address.
     *
     * @param houseName The name of the house.
     * @param houseAddress The address of the house.
     * @return A response indicating the result of the creation operation.
     */
    public Response createHouse(String houseName, String houseAddress) {

        if (houseRepository.findByHouseName(houseName) != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("La casa con questo nome esiste già")
                    .build();
        }

        // Generate a unique house code (for example, using UUID)
        String houseCode = java.util.UUID.randomUUID().toString();
        // Hash the house code for security
        String hashedHouseCode = BCrypt.hashpw(houseCode, BCrypt.gensalt());
        House newHouse = new House(hashedHouseCode, houseName, houseAddress);
        // Save the house to the database
        houseRepository.insert(newHouse);

        return Response.status(Response.Status.CREATED)
                .entity("{\"code\":\"" + houseCode + "\"}")
                .type("application/json")
                .build();
    }

    /**
     * Login a house using its code.
     *
     * @param houseCode The house code to login.
     * @return A response indicating the result of the login operation.
     */
    public Response loginHouse(String houseCode) {
        House house = houseRepository.findAll()
                .stream()
                .filter(h -> BCrypt.checkpw(houseCode, h.getHouseId()))
                .findFirst()
                .orElse(null);

        System.out.println("House Code: " + houseCode);
        houseRepository.findAll().forEach(h -> {
            System.out.println("Stored HouseId: " + h.getHouseId());
            System.out.println("Match: " + BCrypt.checkpw(houseCode, h.getHouseId()));
        });

        if (house == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("codice non valido")
                    .build();
        }

        return Response.ok("Il login alla casa è stato effettuato con successo").build();
    }
}