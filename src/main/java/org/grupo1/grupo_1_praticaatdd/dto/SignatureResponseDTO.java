package org.grupo1.grupo_1_praticaatdd.dto;

import org.grupo1.grupo_1_praticaatdd.domain.Signature;
import org.grupo1.grupo_1_praticaatdd.domain.enums.SignaturePlan;

public record SignatureResponseDTO(
        Long id,
        SignaturePlan plan,
        Integer courseCredits,
        Integer successFinishedCourses,
        Integer coins,
        Long userId
) {
    public SignatureResponseDTO(Signature signature) {
        this(
                signature.getId(),
                signature.getPlan(),
                signature.getCourseCredits(),
                signature.getSuccessFinishedCourses(),
                signature.getCoins(),
                signature.getUser().getId()
        );
    }
}