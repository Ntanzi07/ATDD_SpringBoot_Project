package org.grupo1.grupo_1_praticaatdd.dto;

import org.grupo1.grupo_1_praticaatdd.domain.Course;

public record CourseResponseDTO(
        Long id,
        String title,
        String description
) {
    public CourseResponseDTO(Course course) {
        this(course.getId(), course.getTitle().getValue(), course.getDescription().getValue());
    }
}