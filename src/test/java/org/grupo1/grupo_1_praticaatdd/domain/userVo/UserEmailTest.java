package org.grupo1.grupo_1_praticaatdd.domain.userVo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UserEmailTest {

    private UserEmail email1;
    private UserEmail email2;
    private UserEmail email3;

    @BeforeEach
    void createUserTest() {
        email1 = new UserEmail("joao@teste.com");
        email2 = new UserEmail("joao@teste.com");
        email3 = new UserEmail("maria@teste.com");
    }

    @Test
    void patternValidationTest() {
        assertAll(
                () -> {
                    // Without @
                    assertThrows(IllegalArgumentException.class, () -> new UserEmail("joaoteste.com"));
                },
                () -> {
                    // Without address after @
                    assertThrows(IllegalArgumentException.class, () -> new UserEmail("joao@.com"));
                },
                () -> {
                    // Without .com .br ...
                    assertThrows(IllegalArgumentException.class, () -> new UserEmail("joao@test"));
                },
                () -> {
                    // Empty password
                    assertThrows(IllegalArgumentException.class, () -> new UserEmail(""));
                },
                () -> {
                    // nullable password
                    assertThrows(IllegalArgumentException.class, () -> new UserEmail(null));
                }
        );
    }

    @Test
    void getEmailTest(){
        assertEquals("joao@teste.com", email1.getEmail());
    }

    @Test
    void isEqualTest() {
        assertAll(
                () -> assertEquals(true, email1.equals(email2)),
                () -> assertEquals(false, email1.equals(email3))
        );
    }
    @Test
    void hashCodeTest() {
        assertAll(
                () -> assertEquals(email1.hashCode(), email2.hashCode()),
                () -> assertNotEquals(email1.hashCode(), email3.hashCode())
        );
    }
}
