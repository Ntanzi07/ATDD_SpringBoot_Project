package org.grupo1.grupo_1_praticaatdd.domain.userVo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UserNameTest {

    private UserName name1;
    private UserName name2;
    private UserName name3;

    @BeforeEach
    void createUserTest() {
        name1 = new UserName("Joao");
        name2 = new UserName("Joao");
        name3 = new UserName("Matheus");
    }

    @Test
    void emptyNameTest() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new UserName("")),
                () -> assertThrows(IllegalArgumentException.class, () -> new UserName(null))
        );
    }

    @Test
    void getNameTest(){
        assertEquals("Joao", name1.getName());
    }

    @Test
    void isEqualTest() {
        assertAll(
                () -> assertEquals(true, name1.equals(name2)),
                () -> assertEquals(false, name1.equals(name3))
        );
    }
    @Test
    void hashCodeTest() {
        assertAll(
                () -> assertEquals(name1.hashCode(), name2.hashCode()),
                () -> assertNotEquals(name1.hashCode(), name3.hashCode())
        );
    }

}
