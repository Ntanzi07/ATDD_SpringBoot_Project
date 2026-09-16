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
        assertEquals("joao@teste.com", email1.getValue());
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

    /*
     * Cenario: Quando comparar o objeto com null ou com um objeto de outro tipo
     *          Entao equals retorna false
     *
     * [RED]   nao se aplica: equals ja existia; faltavam os ramos
     *         "o == null" e "getClass() != o.getClass()" (AMARELO no JaCoCo do GREEN).
     * [GREEN] codigo ja existente:
     *             if (o == null || getClass() != o.getClass()) return false;
     * [BLUE]  teste criado para cobrir os dois ramos. Resultado: PASSOU.
     */
    @Test
    void notEqualToNullOrOtherTypeTest() {
        assertAll(
                () -> assertEquals(false, email1.equals(null)),
                () -> assertEquals(false, email1.equals("outro tipo"))
        );
    }
}
