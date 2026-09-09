package org.grupo1.grupo_1_praticaatdd.domain;

import org.grupo1.grupo_1_praticaatdd.domain.enums.SignaturePlan;
import org.grupo1.grupo_1_praticaatdd.domain.userVo.UserEmail;
import org.grupo1.grupo_1_praticaatdd.domain.userVo.UserName;
import org.grupo1.grupo_1_praticaatdd.domain.userVo.UserPassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    private User user1;

    @BeforeEach
    void createUser() {
        user1 = new User("Nathan", "nathan@test.com", "@Link1234567");
    }

    @Test
    void returnAllField() {
        assertAll(
                () -> assertEquals(new UserName("Nathan"), user1.getName()),
                () -> assertEquals(new UserEmail("nathan@test.com"), user1.getEmail()),
                () -> assertEquals(new UserPassword("@Link1234567"), user1.getPassword()),
                () -> assertEquals(SignaturePlan.BASIC, user1.getSignature().getPlan())
        );
    }

    @Test
    void setAllField() {
        assertAll(
                () -> {
                    user1.modifyName("Nathan Tanzi");
                    assertEquals(new UserName("Nathan Tanzi"), user1.getName());
                },
                () -> {
                    user1.modifyEmail("nathan.tanzi@test.com");
                    assertEquals(new UserEmail("nathan.tanzi@test.com"), user1.getEmail());
                },
                () -> {
                    user1.changePassword("@Link176217");
                    assertEquals(new UserPassword("@Link176217"), user1.getPassword());
                }
        );
    }
}
