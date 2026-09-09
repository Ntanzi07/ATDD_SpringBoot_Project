package org.grupo1.grupo_1_praticaatdd.domain;

import org.grupo1.grupo_1_praticaatdd.domain.enums.RegistrationNumberStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationNumberTest {

    private RegistrationNumber registrationNumber;
    private Course course1;
    private User user1;

    @BeforeEach
    void createSignatureTest() {
        user1 = new User("Nathan", "nathan@test.com", "@Link1234567");
        course1 = new Course("JavaWithALeles", "Java class with professor Leles");
        registrationNumber = new RegistrationNumber(user1, course1, false);
    }

    @Test
    void returnAllFieldTest() {
        assertAll(
                () -> assertEquals(user1, registrationNumber.getUser()),
                () -> assertEquals(course1, registrationNumber.getCourse()),
                () -> assertEquals(RegistrationNumberStatus.IN_PROGRESS, registrationNumber.getRegistrationNumberStatus()),
                () -> assertEquals(null, registrationNumber.getFinalGrade()),
                () -> assertEquals(false, registrationNumber.isBonus())
        );
    }

    @Test
    void setAllFieldTest() {
        assertAll(
                () -> {
                    User user2 = new User("exemplo", "exemplo@teste.com", "Exe@1234567");
                    registrationNumber.setUser(user2);
                    assertEquals(user2, registrationNumber.getUser());
                },
                () -> {
                    Course course2 = new Course("DevOpsWithALeles", "DevOps class with professor Leles");
                    registrationNumber.setCourse(course2);
                    assertEquals(course2, registrationNumber.getCourse());
                },
                () -> {
                    registrationNumber.setRegistrationNumberStatus(RegistrationNumberStatus.COMPLETED);
                    assertEquals(RegistrationNumberStatus.COMPLETED, registrationNumber.getRegistrationNumberStatus());
                },
                () -> {
                    registrationNumber.setFinalGrade(8.3);
                    assertEquals(8.3, registrationNumber.getFinalGrade());
                },
                () -> {
                    registrationNumber.setBonus(true);
                    assertEquals(true, registrationNumber.isBonus());
                }
        );
    }
}
