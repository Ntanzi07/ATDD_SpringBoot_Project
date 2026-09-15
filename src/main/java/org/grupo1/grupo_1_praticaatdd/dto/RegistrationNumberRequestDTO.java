package org.grupo1.grupo_1_praticaatdd.dto;

public record RegistrationNumberRequestDTO(
        Long userId,
        Long courseId,
        boolean bonus
) {}