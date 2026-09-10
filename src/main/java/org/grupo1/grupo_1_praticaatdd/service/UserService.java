package org.grupo1.grupo_1_praticaatdd.service;

import org.grupo1.grupo_1_praticaatdd.domain.User;
import org.grupo1.grupo_1_praticaatdd.dto.UserRequestDTO;
import org.grupo1.grupo_1_praticaatdd.dto.UserResponseDTO;
import org.grupo1.grupo_1_praticaatdd.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserService {
    private static final Pattern PATERN_PASSWORD = Pattern.compile("^(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private String encodePassword(String password) {
        if (!PATERN_PASSWORD.matcher(password).matches())
            throw new IllegalArgumentException("password in wrong format, it needs to follow the min 8 length, a Uppercase and and a especial character");

        return passwordEncoder.encode(password);
    }

    public UserResponseDTO createUser(UserRequestDTO request) {
        String hash = encodePassword(request.password());
        User user = new User(request.name(), request.email(), hash);

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(savedUser);
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponseDTO(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(u -> new UserResponseDTO(
                        u.getId(),
                        u.getName().getValue(),
                        u.getEmail().getValue()
                ))
                .toList();
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.name() == null || request.name().isBlank())
            throw new IllegalArgumentException();
        if (request.email() == null || request.email().isBlank())
            throw new IllegalArgumentException();
        if (request.password() == null || request.password().isBlank())
            throw new IllegalArgumentException();

        user.modifyName(request.name());
        user.modifyEmail(request.email());
        user.changePassword(encodePassword(request.password()));

        User updated = userRepository.save(user);
        return new UserResponseDTO(updated);
    }

    public UserResponseDTO deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
        return new UserResponseDTO(user);
    }
}
