package org.grupo1.grupo_1_praticaatdd.dto;

import org.grupo1.grupo_1_praticaatdd.domain.enums.SignaturePlan;

public record SignatureRequestDTO(
        SignaturePlan plan,
        Integer courseCredits,
        Integer successFinishedCourses,
        Integer coins,
        Long userId
) {}