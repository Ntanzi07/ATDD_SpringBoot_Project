package org.grupo1.grupo_1_praticaatdd.domain.userVo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserEncryptedPasswordTest {

    private UserEncryptedPassword password1;
    private UserEncryptedPassword password2;
    private UserEncryptedPassword password3;

    @BeforeEach
    void createUserTest() {
        password1 = new UserEncryptedPassword("Tes@123456");
        password2 = new UserEncryptedPassword("Tes@123456");
        password3 = new UserEncryptedPassword("Tes@654321");
    }

    @Test
    void EmptyHashTest() {
        assertAll(
                () -> {
                    // Empty password
                    assertThrows(IllegalArgumentException.class, () -> new UserEncryptedPassword(""));
                },
                () -> {
                    // nullable password
                    assertThrows(IllegalArgumentException.class, () -> new UserEncryptedPassword(null));
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
                () -> assertEquals(false, password1.equals(null)),
                () -> assertEquals(false, password1.equals("outro tipo"))
        );
    }
}
