package com.coinquyteam.dashboard.Service;

import com.coinquyteam.dashboard.Utility.ClassificaRequest;
import com.coinquyteam.dashboard.Utility.Classifica;
import com.coinquyteam.dashboard.Utility.CoiquyListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;

@Service
public class DashService
{

    @Autowired
    private final RestTemplate restTemplate;

    public DashService(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    public List<?> getCoinquy(String auth, String houseId)
    {
        String url = "http://localhost:8080/Auth/rest/client/retrieveCoinquy?houseId=" + houseId;
        String token = auth.substring(7);
        return makeGetRequest(url,token);
    }

    public List<?> getTurni(String houseId, String auth){
        String url = "http://localhost:8080/Shift/rest/client/retriveShift?houseId=" + houseId;
        String token = auth.substring(7);
        return makeGetRequest(url, token);
    }

    public LinkedHashMap<String, Classifica> getClassifica(CoiquyListDTO coiquyListDTO, String auth) {
        String url = "http://localhost:8080/Rank/rest/client/retrieveClassifica";
        String token = auth.substring(7);
        return makePostRequest(url, coiquyListDTO, token);
    }

    private List<?> makeGetRequest(String url, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                List.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Errore nella chiamata a " + url + ": status code " + response.getStatusCode());
        }
    }

    private LinkedHashMap<String, Classifica> makePostRequest(String url, CoiquyListDTO body, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<CoiquyListDTO> entity = new HttpEntity<>(body, headers);

        ResponseEntity<LinkedHashMap<String, Classifica>> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<>() { }
        );
        System.out.println(response.getBody());
        System.out.println(response.getStatusCode());
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Errore nella chiamata a " + url + ": status code " + response.getStatusCode());
        }
    }
}
