package org.grupo1.grupo_1_praticaatdd.service;

import org.grupo1.grupo_1_praticaatdd.domain.User;
import org.grupo1.grupo_1_praticaatdd.dto.UserRequestDTO;
import org.grupo1.grupo_1_praticaatdd.dto.UserResponseDTO;
import org.grupo1.grupo_1_praticaatdd.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;


    @Test
    void failWhenPasswordHasNoSpecialCharacterTest() {
        UserRequestDTO request = new UserRequestDTO("Nathan", "nathan@test.com", "Senha123");
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(request));
    }

    @Test
    void failWhenPasswordHasNoUppercaseTest() {
        UserRequestDTO request = new UserRequestDTO("Nathan", "nathan@test.com", "senha@123");
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(request));
    }

    @Test
    void failWhenPasswordIsShorterThan8CharactersTest() {
        UserRequestDTO request = new UserRequestDTO("Nathan", "nathan@test.com", "Se@123");
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(request));
    }

    @Test
    void createUserSuccessfullyTest() {
        UserRequestDTO request = new UserRequestDTO("Nathan", "nathan@test.com", "Senha@123");
        User savedUser = new User("Nathan", "nathan@test.com", "Hash@Falso123");

        when(passwordEncoder.encode("Senha@123")).thenReturn("Hash@Falso123");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDTO response = userService.createUser(request);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals("Nathan", response.name()),
                () -> assertEquals("nathan@test.com", response.email())
        );

        verify(passwordEncoder).encode("Senha@123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserByIdSuccessfulTest() {
        User user = new User("Nathan", "nathan@test.com", "Hash@Falso123");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO response = userService.getUserById(1l);
        assertEquals("Nathan", response.name());
        verify(userRepository).findById(1L);
    }

    @Test
    void failWhenNotGetUserByIdTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserById(1L));
        verify(userRepository).findById(1L);
    }

    @Test
    void updateNameUserByIdSuccessfulTest() {
        User existingUser = new User("Nathan", "nathan@test.com", "Hash@Falso123");
        UserRequestDTO request = new UserRequestDTO("Nathan Updated", "nathanupdated@test.com", "NovaSenha@123");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("NovaSenha@123")).thenReturn("NovoHash@Falso");
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserResponseDTO response = userService.updateUser(1L, request);

        assertAll(
                () -> assertEquals("Nathan Updated", response.name()),
                () -> assertEquals("nathanupdated@test.com", response.email())
        );
        verify(userRepository).findById(1L);
        verify(passwordEncoder).encode("NovaSenha@123");
        verify(userRepository).save(any(User.class));

    }

    @Test
    void failWhenUpdateNameUserByIdWithNameNullTest() {
        UserRequestDTO request1 = new UserRequestDTO(null, "nathanupdated@test.com", "NovaSenha@123");
        UserRequestDTO request2 = new UserRequestDTO("", "nathanupdated@test.com", "NovaSenha@123");

        User existingUser = new User("Nathan", "nathan@test.com", "Hash@Falso123");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> userService.updateUser(1L, request1)),
                () -> assertThrows(IllegalArgumentException.class, () -> userService.updateUser(1L, request2))
        );
        verify(userRepository, times(2)).findById(1L);
    }

    @Test
    void failWhenUpdateEmailUserByIdWithNameNullTest() {
        UserRequestDTO request1 = new UserRequestDTO("Nathan Updated", null, "NovaSenha@123");
        UserRequestDTO request2 = new UserRequestDTO("Nathan Updated", "", "NovaSenha@123");

        User existingUser = new User("Nathan", "nathan@test.com", "Hash@Falso123");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> userService.updateUser(1L, request1)),
                () -> assertThrows(IllegalArgumentException.class, () -> userService.updateUser(1L, request2))
        );
        verify(userRepository, times(2)).findById(1L);
    }

    @Test
    void failWhenUpdatePasswordUserByIdWithNameNullTest() {
        UserRequestDTO request1 = new UserRequestDTO("Nathan Updated", "nathanupdated@test.com", null);
        UserRequestDTO request2 = new UserRequestDTO("Nathan Updated", "nathanupdated@test.com", "");

        User existingUser = new User("Nathan", "nathan@test.com", "Hash@Falso123");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> userService.updateUser(1L, request1)),
                () -> assertThrows(IllegalArgumentException.class, () -> userService.updateUser(1L, request2))
        );
        verify(userRepository, times(2)).findById(1L);
    }

    @Test
    void failWhenUpdateUserByIdNotFoundTest() {
        UserRequestDTO request = new UserRequestDTO("Nathan Updated", "nathanupdated@test.com", "NovaSenha@123");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUser(1L, request));
        verify(userRepository).findById(1L);
    }

    @Test
    void deleteUserSuccessfulTest() {
        User user = new User("Nathan", "nathan@test.com", "Hash@Falso123");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO response = userService.deleteUser(1L);

        assertEquals("Nathan", response.name());
        verify(userRepository).findById(1L);
        verify(userRepository).delete(user);
    }

    @Test
    void failWhenDeleteUserNotFoundTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
        verify(userRepository).findById(1L);
    }
}
