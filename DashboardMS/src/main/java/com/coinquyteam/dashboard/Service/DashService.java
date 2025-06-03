package com.coinquyteam.dashboard.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    public List<?> getTurni(String houseId)
    {
        String url = "http://localhost:8080/Shift/rest/client/retriveShift?houseId=" + houseId;
        return makeGetRequest(url);
    }

    public List<?> getClassifica(String houseId)
    {
        String url = "http://localhost:8080/Rank/rest/client/retrieveClassifica?houseId=" + houseId;
        return makeGetRequest(url);
    }

    private List<?> makeGetRequest(String url)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<List> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                List.class
        );

        if (response.getStatusCode() == HttpStatus.OK)
        {
            return response.getBody();
        }
        else
        {
            throw new RuntimeException("Errore nella chiamata a " + url + ": status code " + response.getStatusCode());
        }
    }
}
