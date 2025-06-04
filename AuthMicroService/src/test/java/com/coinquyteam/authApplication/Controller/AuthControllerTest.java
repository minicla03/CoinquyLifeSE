package com.coinquyteam.authApplication.Controller;

import com.coinquyteam.authApplication.Data.User;
import com.coinquyteam.authApplication.Service.AuthService;
import com.coinquyteam.authApplication.Utility.AuthResult;
import com.coinquyteam.authApplication.Utility.StatusAuth;
import com.coinquyteam.authApplication.Utility.UserResult;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccessWithUsername() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        AuthResult result = new AuthResult(StatusAuth.SUCCESS, "token123");
        when(authService.login("testuser", "password")).thenReturn(result);

        Response response = authController.login(user);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("token123"));
    }

    @Test
    void testLoginSuccessWithEmail() {
        User user = new User();
        user.setUsername(null);
        user.setEmail("test@example.com");
        user.setPassword("password");
        AuthResult result = new AuthResult(StatusAuth.SUCCESS, "token456");
        when(authService.login("test@example.com", "password")).thenReturn(result);

        Response response = authController.login(user);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("token456"));
    }

    @Test
    void testLoginUserNotFound() {
        User user = new User();
        user.setUsername("notfound");
        user.setPassword("password");
        AuthResult result = new AuthResult(StatusAuth.USER_NOT_FOUND, null);
        when(authService.login("notfound", "password")).thenReturn(result);

        Response response = authController.login(user);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Utente non trovato", response.getEntity());
    }

    @Test
    void testLoginInvalidCredentials() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("wrongpassword");
        AuthResult result = new AuthResult(StatusAuth.INVALID_CREDENTIALS, null);
        when(authService.login("testuser", "wrongpassword")).thenReturn(result);

        Response response = authController.login(user);

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        assertEquals("Credenziali errate o non valide", response.getEntity());
    }

    @Test
    void testLoginServerError() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        AuthResult result = new AuthResult(null, null);
        when(authService.login("testuser", "password")).thenReturn(result);

        Response response = authController.login(user);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals("Server error", response.getEntity());
    }

    @Test
    void testRegisterSuccess() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setName("Test");
        user.setSurname("User");
        user.setEmail("test@example.com");
        AuthResult result = new AuthResult(StatusAuth.SUCCESS, null);
        when(authService.register(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(result);

        Response response = authController.register(user);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Registration successful", response.getEntity());
    }

    @Test
    void testRegisterInvalidEmail() {
        User user = new User();
        user.setEmail("invalidemail");
        Response response = authController.register(user);

        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        assertEquals("Email invalido", response.getEntity());
    }

    @Test
    void testRegisterUserAlreadyExists() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setName("Test");
        user.setSurname("User");
        user.setEmail("test@example.com");
        AuthResult result = new AuthResult(StatusAuth.USER_ALREADY_EXISTS, null);
        when(authService.register(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(result);

        Response response = authController.register(user);

        assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
        assertEquals("User already exists", response.getEntity());
    }

    @Test
    void testRegisterServerError() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setName("Test");
        user.setSurname("User");
        user.setEmail("test@example.com");
        AuthResult result = new AuthResult(null, null);
        when(authService.register(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(result);

        Response response = authController.register(user);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals("An error occurred", response.getEntity());
    }

    @Test
    void testGetUserByHouseIdUsersFound() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("houseId", "HOUSE123");
        UserResult result = mock(UserResult.class);
        when(result.getStatusAuth()).thenReturn(StatusAuth.USERS_FOUNDED);
        when(result.getUsers()).thenReturn(Arrays.asList(new User(), new User()));
        when(authService.getUserByHouseId("HOUSE123")).thenReturn(result);

        Response response = authController.getUserByHouseId(requestBody);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof List);
    }

    @Test
    void testGetUserByHouseIdUsersNotFound() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("houseId", "HOUSE123");
        UserResult result = mock(UserResult.class);
        when(result.getStatusAuth()).thenReturn(StatusAuth.USERS_NOT_FOUND);
        when(authService.getUserByHouseId("HOUSE123")).thenReturn(result);

        Response response = authController.getUserByHouseId(requestBody);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("No users found for the given house ID", response.getEntity());
    }
}