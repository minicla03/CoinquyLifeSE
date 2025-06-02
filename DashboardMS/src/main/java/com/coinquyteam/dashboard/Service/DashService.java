package com.coinquyteam.dashboard.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class DashService
{
    @Autowired
    private final RestTemplate restTemplate;

    public List<?> getCoinquy(String houseId)
    {
        String COINQUY_SERVICE_URL = "http://microservizio-coinquy/api/coinquy?houseId=";
        return restTemplate.getForObject(COINQUY_SERVICE_URL + houseId, List.class);
    }

    public List<?> getTurni(String houseId)
    {
        String TURNI_SERVICE_URL = "http://microservizio-turni/api/turni?houseId=";
        return restTemplate.getForObject(TURNI_SERVICE_URL + houseId, List.class);
    }

    public List<?> getClassifica(String houseId)
    {
        String CLASSIFICA_SERVICE_URL = "http://microservizio-classifica/api/classifica?houseId=";
        return restTemplate.getForObject(CLASSIFICA_SERVICE_URL + houseId, List.class);
    }
}

/*String url = "http://localhost:8081/rest/auth/external/link-house";
HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
Map<String, String> body = Map.of(
        "houseCode", houseCode);
HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

ResponseEntity<String> response;
        try {
response = restTemplate.postForEntity(url, request, String.class);
        } */
