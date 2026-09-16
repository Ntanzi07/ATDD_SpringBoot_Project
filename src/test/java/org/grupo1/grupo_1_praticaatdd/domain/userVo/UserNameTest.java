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
        assertEquals("Joao", name1.getValue());
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
                () -> assertEquals(false, name1.equals(null)),
                () -> assertEquals(false, name1.equals("outro tipo"))
        );
    }
}
