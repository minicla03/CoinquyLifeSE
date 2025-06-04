package com.coinquyteam.authApplication.Repository;

import com.coinquyteam.authApplication.Data.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("IUserRepository Tests")
class IUserRepositoryTest {

    @Mock
    private IUserRepository userRepository;

    private User testUser;
    private User testUser2;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "Test", "password", "User", "test@example.com");
        testUser.setId_user("123456");
        testUser.setHouseUser("HOUSE123");

        testUser2 = new User("testuser2", "Test2", "password2", "User2", "test2@example.com");
        testUser2.setId_user("789012");
        testUser2.setHouseUser("HOUSE123");
    }

    @Test
    @DisplayName("Trova user da email")
    void testFindUserByEmail() {
        // Given
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(testUser);

        // When
        User foundUser = userRepository.findByEmail(email);

        // Then
        assertNotNull(foundUser);
        assertEquals(testUser.getEmail(), foundUser.getEmail());
        assertEquals(testUser.getUsername(), foundUser.getUsername());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("Return null quando l'utente non viene trovato per email")
    void testReturnNullWhenUserNotFoundByEmail() {
        // Given
        String nonExistentEmail = "nonexistent@example.com";
        when(userRepository.findByEmail(nonExistentEmail)).thenReturn(null);

        // When
        User foundUser = userRepository.findByEmail(nonExistentEmail);

        // Then
        assertNull(foundUser);
        verify(userRepository, times(1)).findByEmail(nonExistentEmail);
    }

    @Test
    @DisplayName("Trova user da username")
    void shouldFindUserByUsername() {
        // Given
        String username = "testuser";
        when(userRepository.findByUsername(username)).thenReturn(testUser);

        // When
        User foundUser = userRepository.findByUsername(username);

        // Then
        assertNotNull(foundUser);
        assertEquals(testUser.getUsername(), foundUser.getUsername());
        assertEquals(testUser.getEmail(), foundUser.getEmail());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    @DisplayName("Return null quando l'utente non viene trovato per username")
    void testReturnNullWhenUserNotFoundByUsername() {
        // Given
        String nonExistentUsername = "nonexistentuser";
        when(userRepository.findByUsername(nonExistentUsername)).thenReturn(null);

        // When
        User foundUser = userRepository.findByUsername(nonExistentUsername);

        // Then
        assertNull(foundUser);
        verify(userRepository, times(1)).findByUsername(nonExistentUsername);
    }

    @Test
    @DisplayName("Trova user da id_user")
    void testFindUserByIdUser() {
        // Given
        String idUser = "123456";
        when(userRepository.findByIdUser(idUser)).thenReturn(testUser);

        // When
        User foundUser = userRepository.findByIdUser(idUser);

        // Then
        assertNotNull(foundUser);
        assertEquals(testUser.getId_user(), foundUser.getId_user());
        verify(userRepository, times(1)).findByIdUser(idUser);
    }

    @Test
    @DisplayName("Trova user da HouseUser")
    void testFindUserByHouseUser() {
        // Given
        String houseUser = "HOUSE123";
        when(userRepository.findByHouseUser(houseUser)).thenReturn(testUser);

        // When
        User foundUser = userRepository.findByHouseUser(houseUser);

        // Then
        assertNotNull(foundUser);
        assertEquals(testUser.getHouseUser(), foundUser.getHouseUser());
        verify(userRepository, times(1)).findByHouseUser(houseUser);
    }

    @Test
    @DisplayName("Set HouseUser per un datousername")
    void testSetHouseUserForUsername() {
        // Given
        String username = "testuser";
        String houseCode = "NEWHOUSE456";

        // When
        userRepository.setHouseUser(username, houseCode);

        // Then
        verify(userRepository, times(1)).setHouseUser(username, houseCode);
    }

    @Test
    @DisplayName("Trova utenti per houseId")
    void testFindUsersByHouseId() {
        // Given
        String houseId = "HOUSE123";
        List<User> expectedUsers = Arrays.asList(testUser, testUser2);
        when(userRepository.findByHouseId(houseId)).thenReturn(expectedUsers);

        // When
        List<User> foundUsers = userRepository.findByHouseId(houseId);

        // Then
        assertNotNull(foundUsers);
        assertEquals(2, foundUsers.size());
        assertTrue(foundUsers.contains(testUser));
        assertTrue(foundUsers.contains(testUser2));
        verify(userRepository, times(1)).findByHouseId(houseId);
    }

}