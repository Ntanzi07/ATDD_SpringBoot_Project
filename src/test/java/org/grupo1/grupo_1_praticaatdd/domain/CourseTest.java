package org.grupo1.grupo_1_praticaatdd.domain;

import org.grupo1.grupo_1_praticaatdd.domain.courseVo.CourseDescription;
import org.grupo1.grupo_1_praticaatdd.domain.courseVo.CourseTitle;
import org.grupo1.grupo_1_praticaatdd.domain.enums.RegistrationNumberStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CourseTest {
    private Course course1;

    @BeforeEach
    void createSignature() {
        course1 = new Course("JavaWithALeles", "Java class with professor Leles");
    }

    @Test
    void returnAllField() {
        assertAll(
                () -> assertEquals(new CourseTitle("JavaWithALeles"), course1.getTitle()),
                () -> assertEquals(new CourseDescription("Java class with professor Leles"), course1.getDescription())
        );
    }

    @Test
    void setAllField() {
        assertAll(
                () -> {
                    course1.changeTitle("DevOpsWithLeles");
                    assertEquals(new CourseTitle("DevOpsWithLeles"), course1.getTitle());
                },
                () -> {
                    course1.changeDescription("DevOps class with professor Leles");
                    assertEquals(new CourseDescription("DevOps class with professor Leles"), course1.getDescription());
                }
        );
    }
}
