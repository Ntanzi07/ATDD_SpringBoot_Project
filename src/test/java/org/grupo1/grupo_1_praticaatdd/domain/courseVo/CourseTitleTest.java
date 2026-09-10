package org.grupo1.grupo_1_praticaatdd.domain.courseVo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CourseTitleTest {
    private CourseTitle courseTitle1;
    private CourseTitle courseTitle2;
    private CourseTitle courseTitle3;

    @BeforeEach
    void createUserTest() {
        courseTitle1 = new CourseTitle("Java Applications");
        courseTitle2 = new CourseTitle("Java Applications");
        courseTitle3 = new CourseTitle("Go Applications");
    }

    @Test
    void emptyCourseTitleTest() {
        assertAll(
                () -> {
                    // Empty password
                    assertThrows(IllegalArgumentException.class, () -> new CourseTitle(""));
                },
                () -> {
                    // nullable password
                    assertThrows(IllegalArgumentException.class, () -> new CourseTitle(null));
                }
        );
    }

    @Test
    void getcourseTitleTest(){
        assertEquals("Java Applications", courseTitle1.getValue());
    }

    @Test
    void isEqualTest() {
        assertAll(
                () -> assertEquals(true, courseTitle1.equals(courseTitle2)),
                () -> assertEquals(false, courseTitle1.equals(courseTitle3))
        );
    }
    @Test
    void hashCodeTest() {
        assertAll(
                () -> assertEquals(courseTitle1.hashCode(), courseTitle2.hashCode()),
                () -> assertNotEquals(courseTitle1.hashCode(), courseTitle3.hashCode())
        );
    }
}
