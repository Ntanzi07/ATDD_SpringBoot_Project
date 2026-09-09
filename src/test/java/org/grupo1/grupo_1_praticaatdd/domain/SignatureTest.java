package org.grupo1.grupo_1_praticaatdd.domain;

import org.grupo1.grupo_1_praticaatdd.domain.enums.SignaturePlan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SignatureTest {

    private Signature signature;
    private User user1;

    @BeforeEach
    void createSignatureTest() {
        user1 = new User("Nathan", "nathan@test.com", "@Link1234567");
        signature = new Signature(user1);
    }

    @Test
    void returnAllFieldTest() {
        assertAll(
                () -> assertEquals(SignaturePlan.BASIC, signature.getPlan()),
                () -> assertEquals(0, signature.getCourseCredits()),
                () -> assertEquals(0, signature.getSuccessFinishedCourses()),
                () -> assertEquals(0, signature.getCoins()),
                () -> assertEquals(user1, signature.getUser())
        );
    }

    @Test
    void setAllFieldTest() {
        assertAll(
                () -> {
                    signature.setPlan(SignaturePlan.PREMIUM);
                    assertEquals(SignaturePlan.PREMIUM, signature.getPlan());
                },
                () -> {
                    signature.setCourseCredits(10);
                    assertEquals(10, signature.getCourseCredits());
                },
                () -> {
                    signature.setSuccessFinishedCourses(10);
                    assertEquals(10, signature.getSuccessFinishedCourses());
                },
                () -> {
                    signature.setCoins(10);
                    assertEquals(10, signature.getCoins());
                },
                () -> {
                    User user2 = new User("exemplo", "exemplo@teste.com", "Exe@1234567");
                    signature.setUser(user2);
                    assertEquals(user2, signature.getUser());
                }
        );
    }
}
