package org.grupo1.grupo_1_praticaatdd.dto;

import org.grupo1.grupo_1_praticaatdd.domain.RegistrationNumber;
import org.grupo1.grupo_1_praticaatdd.domain.enums.RegistrationNumberStatus;

public record RegistrationNumberResponseDTO(
        Long id,
        Long userId,
        Long courseId,
        RegistrationNumberStatus status,
        Double finalGrade,
        boolean bonus
) {
    public RegistrationNumberResponseDTO(RegistrationNumber registrationNumber) {
        this(
                registrationNumber.getId(),
                registrationNumber.getUser().getId(),
                registrationNumber.getCourse().getId(),
                registrationNumber.getRegistrationNumberStatus(),
                registrationNumber.getFinalGrade(),
                registrationNumber.isBonus()
        );
    }
}