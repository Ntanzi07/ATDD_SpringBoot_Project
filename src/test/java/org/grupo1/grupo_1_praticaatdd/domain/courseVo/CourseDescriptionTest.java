package org.grupo1.grupo_1_praticaatdd.domain.courseVo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CourseDescriptionTest {
    private CourseDescription description1;
    private CourseDescription description2;
    private CourseDescription description3;

    @BeforeEach
    void createUserTest() {
        description1 = new CourseDescription("Java class for all");
        description2 = new CourseDescription("Java class for all");
        description3 = new CourseDescription("Go class for all");
    }

    @Test
    void getCourseDescriptionTest(){
        assertEquals("Java class for all", description1.getValue());
    }

    @Test
    void isEqualTest() {
        assertAll(
                () -> assertEquals(true, description1.equals(description2)),
                () -> assertEquals(false, description1.equals(description3))
        );
    }
    @Test
    void hashCodeTest() {
        assertAll(
                () -> assertEquals(description1.hashCode(), description2.hashCode()),
                () -> assertNotEquals(description1.hashCode(), description3.hashCode())
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
                () -> assertEquals(false, description1.equals(null)),
                () -> assertEquals(false, description1.equals("outro tipo"))
        );
    }

    /*
     * Cenario: Quando criar a descricao com null
     *          Entao o valor fica null (descricao e opcional)
     *
     * [RED]   nao se aplica: o ternario ja existia; faltava o ramo "description == null"
     *         (AMARELO no JaCoCo do GREEN).
     * [GREEN] codigo ja existente:
     *             this.description = description == null ? null : description.trim();
     * [BLUE]  teste criado para cobrir o ramo. Resultado: PASSOU.
     */
    @Test
    void nullDescriptionTest() {
        assertEquals(null, new CourseDescription(null).getValue());
    }
}
