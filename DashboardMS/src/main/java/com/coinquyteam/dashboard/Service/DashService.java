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

    public List<?> getCoinquy(String houseId)
    {
        String url = "http://localhost:8080/Auth/rest/client/retrieveCoinquy?houseId=" + houseId;
        return makeGetRequest(url);
    }

    public List<?> getTurni(String houseId) {
        String url = "http://localhost:8080/Shift/rest/client/retriveShift?houseId=" + houseId;
        return makeGetRequest(url);
    }

    public LinkedHashMap<String, Classifica> getClassifica(CoiquyListDTO coiquyListDTO) {
        String url = "http://localhost:8080/Rank/rest/client/retrieveClassifica";
        return makePostRequest(url, coiquyListDTO);
    }

    private List<?> makeGetRequest(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
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

    private LinkedHashMap<String, Classifica> makePostRequest(String url, CoiquyListDTO body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
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
