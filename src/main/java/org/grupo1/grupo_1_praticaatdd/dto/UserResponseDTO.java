package org.grupo1.grupo_1_praticaatdd.dto;

import org.grupo1.grupo_1_praticaatdd.domain.User;

public record UserResponseDTO(
        Long id,
        String name,
        String email
) {
    public UserResponseDTO(User user) {
        this(user.getId(), user.getName().getValue(), user.getEmail().getValue());
    }
}