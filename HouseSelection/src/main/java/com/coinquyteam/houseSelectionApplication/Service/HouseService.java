package com.coinquyteam.houseSelectionApplication.Service;

import com.coinquyteam.houseSelectionApplication.Data.House;
import com.coinquyteam.houseSelectionApplication.Repository.IHouseRepository;
import com.coinquyteam.houseSelectionApplication.Utility.HouseResult;
import com.coinquyteam.houseSelectionApplication.Utility.HouseStatus;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service("HouseService")
public class HouseService {
    @Autowired
    private IHouseRepository houseRepository;
    @Autowired
    private RestTemplate restTemplate;

    public HouseResult createHouse(String houseName, String houseAddress) {
        assert houseName != null;
        if (houseRepository.findByHouseName(houseName) != null) {
            return new HouseResult(HouseStatus.HOUSE_ALREADY_EXISTS, "La casa con questo nome esiste già");
        }

        String houseCode = java.util.UUID.randomUUID().toString();
        String hashedHouseCode = BCrypt.hashpw(houseCode, BCrypt.gensalt());
        House newHouse = new House(hashedHouseCode, houseName, houseAddress);
        try {
            houseRepository.insert(newHouse);
            return new HouseResult(HouseStatus.HOUSE_CREATED, houseCode);
        } catch (Exception e) {
            return new HouseResult(HouseStatus.HOUSE_NOT_CREATED, "Errore: " + e.getMessage());
        }
    }

    public HouseResult loginHouse(String houseCode) {
        House house = houseRepository.findAll()
                .stream()
                .filter(h -> BCrypt.checkpw(houseCode, h.getHouseId()))
                .findFirst()
                .orElse(null);

        if (house == null) {
            return new HouseResult(HouseStatus.HOUSE_NOT_FOUND, "codice non valido");
        } else {
            return new HouseResult(HouseStatus.HOUSE_LOGGED_IN, house.getHouseId());
        }
    }

    public HouseResult linkHouseToUser(String token, String houseCode)
    {
        String url = "http://localhost:8081/rest/auth/external/link-house";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        Map<String, String> body = Map.of(
                "houseCode", houseCode);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);


        if (response.getStatusCode().is2xxSuccessful())
        {
            return new HouseResult(HouseStatus.LINKED_SUCCES, "House linked successfully");
        }
        else if (response.getStatusCode().is4xxClientError())
        {
            return new HouseResult(HouseStatus.USER_NOT_FOUND, "User not found");
        }
        else
        {
            System.out.println("Errore");
            return new HouseResult(HouseStatus.LINKED_ERROR, "Failed to link house");
        }
    }
}