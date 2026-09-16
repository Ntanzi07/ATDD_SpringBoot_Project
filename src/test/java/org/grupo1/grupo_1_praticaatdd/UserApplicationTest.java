package org.grupo1.grupo_1_praticaatdd;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class UserApplicationTest {

    @Test
    void mainStartsSpringApplicationTest() {
        String[] args = {};
        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {
            UserApplication.main(args);

            springApplication.verify(() -> SpringApplication.run(UserApplication.class, args));
        }
    }
}
