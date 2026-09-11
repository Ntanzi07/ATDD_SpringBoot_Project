package org.grupo1.grupo_1_praticaatdd.repository;

import org.grupo1.grupo_1_praticaatdd.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}