package com.coinquyteam.authApplication.Controller;

import com.coinquyteam.authApplication.Data.User;
import com.coinquyteam.authApplication.Service.AuthService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private ClientController clientController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveCoinquySuccess() {
        String houseId = "house123";
        User user1 = new User();
        user1.setUsername("user1");
        User user2 = new User();
        user2.setUsername("user2");
        List<User> coinquilini = Arrays.asList(user1, user2);

        when(authService.getCoinquilinibyHouseId(houseId)).thenReturn(coinquilini);

        Response response = clientController.retrieveCoinquy(houseId);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(coinquilini, response.getEntity());
    }

    @Test
    void testRetrieveCoinquyMissingHouseId() {
        Response response = clientController.retrieveCoinquy(null);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("Missing houseId parameter"));
    }
}