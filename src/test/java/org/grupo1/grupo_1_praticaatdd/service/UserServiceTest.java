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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    }

    @Test
    void failWhenNotGetUserByIdTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserById(1L));
    }

    @Test
    void updateUserByIdSuccessfulTest(){
        User user = new User("Nathan", "nathan@test.com", "Hash@Falso123");
        when(userRepository.save(any(User.class))).thenReturn(user);
    }
}
