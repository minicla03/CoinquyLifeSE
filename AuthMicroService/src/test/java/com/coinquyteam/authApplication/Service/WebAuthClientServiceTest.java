package com.coinquyteam.authApplication.Service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.coinquyteam.authApplication.Data.User;
import com.coinquyteam.authApplication.Repository.IUserRepository;
import com.coinquyteam.authApplication.Utility.AuthResult;
import com.coinquyteam.authApplication.Utility.StatusAuth;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WebAuthClientServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock(answer = RETURNS_DEEP_STUBS) // che permette di simulare chiamate nidificate utile per testare metodi che dipendono da chiamate HTTP simulate
    private WebClient webClient;

    @InjectMocks
    private WebAuthClientService webAuthClientService;

    @Test
    void linkHouseToUser_NullTokenOrHouseCode_ReturnsInvalidCredentials() {
        AuthResult result1 = webAuthClientService.linkHouseToUser(null, "house");
        AuthResult result2 = webAuthClientService.linkHouseToUser("token", null);
        assertEquals(StatusAuth.INVALID_CREDENTIALS, result1.getStatusAuth());
        assertEquals(StatusAuth.INVALID_CREDENTIALS, result2.getStatusAuth());
    }

    @Test
    void linkHouseToUser_InvalidToken_ReturnsUserNotFound() {
        when(webClient.post().uri(anyString()).bodyValue(any()).retrieve().bodyToMono(Map.class).block())
                .thenReturn(null);
        AuthResult result = webAuthClientService.linkHouseToUser("invalidtoken", "house");
        assertEquals(StatusAuth.USER_NOT_FOUND, result.getStatusAuth());
    }

    @Test
    void linkHouseToUser_UserNotFound_ReturnsUserNotFound() {
        when(webClient.post().uri(anyString()).bodyValue(any()).retrieve().bodyToMono(Map.class).block())
                .thenReturn(Map.of("username", "user"));
        when(userRepository.findByUsername("user")).thenReturn(null);
        AuthResult result = webAuthClientService.linkHouseToUser("token", "house");
        assertEquals(StatusAuth.USER_NOT_FOUND, result.getStatusAuth());
    }

    @ParameterizedTest
    @CsvSource({
            "true, SUCCESS",
            "false, USER_ALREADY_LINKED"
    })
    void linkHouseToUser_UserAlreadyLinked_VariousCases(boolean checkpwResult, StatusAuth expectedStatus) {
        User user = mock(User.class);
        when(user.getHouseUser()).thenReturn("hashedHouse");
        when(webClient.post().uri(anyString()).bodyValue(any()).retrieve().bodyToMono(Map.class).block())
                .thenReturn(Map.of("username", "user"));
        when(userRepository.findByUsername("user")).thenReturn(user);

        try (var mocked = org.mockito.Mockito.mockStatic(BCrypt.class)) {
            mocked.when(() -> BCrypt.checkpw(eq("house"), anyString())).thenReturn(checkpwResult);
            AuthResult result = webAuthClientService.linkHouseToUser("token", "house");
            assertEquals(expectedStatus, result.getStatusAuth());
        }
    }

    @Test
    void linkHouseToUser_SuccessfulLink_ReturnsSuccess() {
        User user = mock(User.class);
        when(user.getUsername()).thenReturn("user");
        when(user.getHouseUser()).thenReturn(null);
        when(webClient.post().uri(anyString()).bodyValue(any()).retrieve().bodyToMono(Map.class).block())
                .thenReturn(Map.of("username", "user"));
        when(userRepository.findByUsername("user")).thenReturn(user);

        AuthResult result = webAuthClientService.linkHouseToUser("token", "house");
        assertEquals(StatusAuth.SUCCESS, result.getStatusAuth());
        verify(userRepository, times(1)).setHouseUser(eq("user"), anyString());
    }
}
