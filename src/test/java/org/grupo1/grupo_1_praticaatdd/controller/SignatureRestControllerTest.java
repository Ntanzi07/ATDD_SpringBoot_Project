package org.grupo1.grupo_1_praticaatdd.controller;

import org.grupo1.grupo_1_praticaatdd.domain.User;
import org.grupo1.grupo_1_praticaatdd.domain.enums.SignaturePlan;
import org.grupo1.grupo_1_praticaatdd.dto.SignatureRequestDTO;
import org.grupo1.grupo_1_praticaatdd.repository.SignatureRepository;
import org.grupo1.grupo_1_praticaatdd.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SignatureRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SignatureRepository signatureRepository;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    void getAllSignaturesTest() throws Exception {
        // signature é criada automaticamente com o user
        userRepository.save(new User("Amanda", "amanda@test.com", "Hash@123456"));
        userRepository.save(new User("Marisol", "marisol@test.com", "Hash@654321"));

        mockMvc.perform(get("/signatures"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getSignatureByIdTest() throws Exception {
        User user = userRepository.save(new User("Amanda", "amanda@test.com", "Hash@123456"));
        Long sigId = user.getSignature().getId();

        mockMvc.perform(get("/signatures/" + sigId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plan").value("BASIC"))
                .andExpect(jsonPath("$.courseCredits").value(0))
                .andExpect(jsonPath("$.coins").value(0));
    }

    @Test
    void updateSignatureTest() throws Exception {
        User user = userRepository.save(new User("Amanda", "amanda@test.com", "Hash@123456"));
        Long sigId = user.getSignature().getId();

        SignatureRequestDTO request = new SignatureRequestDTO(
                SignaturePlan.PREMIUM, 10, 5, 100, null);

        mockMvc.perform(put("/signatures/" + sigId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plan").value("PREMIUM"))
                .andExpect(jsonPath("$.courseCredits").value(10))
                .andExpect(jsonPath("$.successFinishedCourses").value(5))
                .andExpect(jsonPath("$.coins").value(100));
    }

    @Test
    void deleteSignatureTest() throws Exception {
        User user = userRepository.save(new User("Amanda", "amanda@test.com", "Hash@123456"));
        Long sigId = user.getSignature().getId();

        mockMvc.perform(delete("/signatures/" + sigId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plan").value("BASIC"));

        assertEquals(0, signatureRepository.count());
    }

    @Test
    void failWhenGetNotFoundTest() throws Exception {
        mockMvc.perform(get("/signatures/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void failWhenPutNotFoundTest() throws Exception {
        long idQueNaoExiste = 999L;
        SignatureRequestDTO request = new SignatureRequestDTO(
                SignaturePlan.PREMIUM, 10, 5, 100, null);

        mockMvc.perform(put("/signatures/" + idQueNaoExiste)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void failWhenDeleteNotFoundTest() throws Exception {
        mockMvc.perform(delete("/signatures/999"))
                .andExpect(status().isNotFound());
    }
}