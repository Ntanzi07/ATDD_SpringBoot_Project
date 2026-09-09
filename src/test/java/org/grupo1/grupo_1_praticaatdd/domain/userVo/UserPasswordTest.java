package org.grupo1.grupo_1_praticaatdd.domain.userVo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserPasswordTest {

    private UserPassword password1;
    private UserPassword password2;
    private UserPassword password3;

    @BeforeEach
    void createUserTest() {
        password1 = new UserPassword("Tes@123456");
        password2 = new UserPassword("Tes@123456");
        password3 = new UserPassword("Tes@654321");
    }

    @Test
    void patternValidationTest() {
        assertAll(
                () -> {
                    // Without especial numbers
                    assertThrows(IllegalArgumentException.class, () -> new UserPassword("Tes12345678"));
                    },
                () -> {
                    //Less than 8 characters
                    assertThrows(IllegalArgumentException.class, () -> new UserPassword("123456"));
                },
                () -> {
                    // Without Uppercase character
                    assertThrows(IllegalArgumentException.class, () -> new UserPassword("@te1234567"));
                },
                () -> {
                    // Empty password
                    assertThrows(IllegalArgumentException.class, () -> new UserPassword(""));
                },
                () -> {
                    // nullable password
                    assertThrows(IllegalArgumentException.class, () -> new UserPassword(null));
                }
        );
    }

    @Test
    void isEqualTest() {
        assertAll(
                () -> assertEquals(true, password1.equals(password2)),
                () -> assertEquals(false, password1.equals(password3))
        );
    }
    @Test
    void hashCodeTest() {
        assertAll(
                () -> assertEquals(password1.hashCode(), password2.hashCode()),
                () -> assertNotEquals(password1.hashCode(), password3.hashCode())
        );
    }
}
