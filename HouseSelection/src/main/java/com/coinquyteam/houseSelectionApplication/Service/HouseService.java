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
            return new HouseResult(HouseStatus.HOUSE_FOUND, house.getHouseId());
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

        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(url, request, String.class);
        } catch (org.springframework.web.client.HttpClientErrorException.Conflict e) {
            // Gestione specifica per 409 Conflict
            return new HouseResult(HouseStatus.USER_ALREADY_LINKED, "User already linked to a house");
        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            // Gestione specifica per 404 Not Found
            return new HouseResult(HouseStatus.USER_NOT_FOUND, "User not found");
        } catch (Exception e) {
            return new HouseResult(HouseStatus.LINKED_ERROR, "Failed to link house");
        }

        if (response.getStatusCode().is2xxSuccessful())
        {
            return new HouseResult(HouseStatus.LINKED_SUCCES, "House linked successfully");
        }
        else
        {
            return new HouseResult(HouseStatus.LINKED_ERROR, "Failed to link house");
        }
    }

    public HouseResult deleteHouse(String houseCode) {

        House house = houseRepository.findAll()
                .stream()
                .filter(h -> BCrypt.checkpw(houseCode, h.getHouseId()))
                .findFirst()
                .orElse(null);

        if (house == null) {
            return new HouseResult(HouseStatus.HOUSE_NOT_FOUND, "House not found");
        }
        try {
            houseRepository.delete(house);
            return new HouseResult(HouseStatus.HOUSE_DELETED, "House deleted successfully");
        } catch (Exception e) {
            return new HouseResult(HouseStatus.HOUSE_NOT_CREATED, "Error deleting house: " + e.getMessage());
        }
    }
}