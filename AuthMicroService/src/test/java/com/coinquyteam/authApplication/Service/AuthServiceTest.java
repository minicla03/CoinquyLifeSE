package com.coinquyteam.authApplication.Service;

import com.coinquyteam.authApplication.Data.User;
import com.coinquyteam.authApplication.Repository.IUserRepository;
import com.coinquyteam.authApplication.Utility.AuthResult;
import com.coinquyteam.authApplication.Utility.StatusAuth;
import com.coinquyteam.authApplication.Utility.UserResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private IUserRepository userRepository;
    private WebClient webClient;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = mock(IUserRepository.class);
        webClient = mock(WebClient.class, RETURNS_DEEP_STUBS);
        authService = new AuthService(userRepository, webClient);
    }

    @Test
    void register_UserAlreadyExists_ReturnsUserAlreadyExists() {
        when(userRepository.findByUsername("user")).thenReturn(new User());
        AuthResult result = authService.register("user", "nome", "pass", "cognome", "email@test.com");
        assertEquals(StatusAuth.USER_ALREADY_EXISTS, result.getStatusAuth());
    }

    @Test
    void register_NewUser_ReturnsSuccess() {
        when(userRepository.findByUsername("user")).thenReturn(null);
        when(webClient.post().uri(anyString()).bodyValue(any()).retrieve().bodyToMono(Map.class).block())
                .thenReturn(Map.of("token", "testtoken"));

        AuthResult result = authService.register("user", "nome", "pass", "cognome", "email@test.com");
        assertEquals(StatusAuth.SUCCESS, result.getStatusAuth());
        assertEquals("testtoken", result.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void login_UserNotFound_ReturnsUserNotFound() {
        when(userRepository.findByUsername("user")).thenReturn(null);
        when(userRepository.findByEmail("user")).thenReturn(null);
        AuthResult result = authService.login("user", "pass");
        assertEquals(StatusAuth.USER_NOT_FOUND, result.getStatusAuth());
    }

    @Test
    void login_InvalidPassword_ReturnsInvalidCredentials() {
        User user = new User("user", "nome", BCrypt.hashpw("rightpass", BCrypt.gensalt()), "cognome", "email@test.com");
        when(userRepository.findByUsername("user")).thenReturn(user);
        AuthResult result = authService.login("user", "wrongpass");
        assertEquals(StatusAuth.INVALID_CREDENTIALS, result.getStatusAuth());
    }

    @Test
    void login_ValidCredentials_ReturnsSuccess() {
        String password = "rightpass";
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User("user", "nome", hashed, "cognome", "email@test.com");
        when(userRepository.findByUsername("user")).thenReturn(user);
        when(webClient.post().uri(anyString()).bodyValue(any()).retrieve().bodyToMono(Map.class).block())
                .thenReturn(Map.of("token", "testtoken"));

        AuthResult result = authService.login("user", password);
        assertEquals(StatusAuth.SUCCESS, result.getStatusAuth());
        assertEquals("testtoken", result.getToken());
    }

    @Test
    void getUserByHouseId_UsersFound_ReturnsUsersFounded() {
        String houseId = "house";
        String hashedHouseId = BCrypt.hashpw(houseId, BCrypt.gensalt());
        User user = mock(User.class);
        when(user.getHouseUser()).thenReturn(hashedHouseId);
        when(userRepository.findAll()).thenReturn(List.of(user));
        // For test, force BCrypt.checkpw to return true
        try (var mocked = Mockito.mockStatic(BCrypt.class)) {
            mocked.when(() -> BCrypt.checkpw(eq(houseId), anyString())).thenReturn(true);
            UserResult result = authService.getUserByHouseId(houseId);
            assertEquals(StatusAuth.USERS_FOUNDED, result.getStatusAuth());
            assertNotNull(result.getUsers());
        }
    }

    @Test
    void getCoinquilinibyHouseId_UsersFound_ReturnsList() {
        String houseId = "house";
        String hashedHouseId = BCrypt.hashpw(houseId, BCrypt.gensalt());
        User user = mock(User.class);
        when(user.getHouseUser()).thenReturn(hashedHouseId);
        when(userRepository.findAll()).thenReturn(List.of(user));
        // Mock BCrypt.checkpw per restituire true
        try (var mocked = Mockito.mockStatic(BCrypt.class)) {
            mocked.when(() -> BCrypt.checkpw(eq(houseId), anyString())).thenReturn(true);
            List<User> result = authService.getCoinquilinibyHouseId(houseId);
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(user, result.get(0));
        }
    }
}