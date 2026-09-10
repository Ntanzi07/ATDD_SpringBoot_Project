package org.grupo1.grupo_1_praticaatdd.controller;

import org.grupo1.grupo_1_praticaatdd.dto.UserRequestDTO;
import org.grupo1.grupo_1_praticaatdd.dto.UserResponseDTO;
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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    void createAndPersistUserSuccessfullyTest() throws Exception {
        UserRequestDTO request = new UserRequestDTO("Nathan", "nathan@test.com", "Senha@123");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Nathan"))
                .andExpect(jsonPath("$.email").value("nathan@test.com"));
    }

    @Test
    void getAllUsersSuccessfullyTest() throws Exception {
        UserRequestDTO request1 = new UserRequestDTO("Nathan", "nathan@test.com", "Senha@123");
        UserRequestDTO request2 = new UserRequestDTO("Lucas", "lucas@test.com", "Senha@123");

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)));

        // agora busca todos e confere se os dois aparecem
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }


    @Test
    void getUserByIdSuccessfullyTest() throws Exception {
        UserRequestDTO request = new UserRequestDTO("Nathan", "nathan@test.com", "Senha@123");

        String responseBody = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserResponseDTO created = objectMapper.readValue(responseBody, UserResponseDTO.class);

        mockMvc.perform(get("/users/" + created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nathan"))
                .andExpect(jsonPath("$.email").value("nathan@test.com"));
    }

    @Test
    void failWhenGetUserByIdNotFoundTest() throws Exception {
        long nonExistentId = 999L;

        mockMvc.perform(get("/users/" + nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void putSuccessfullyTest() throws Exception {
        UserRequestDTO request1 = new UserRequestDTO("Nathan", "nathan@test.com", "Senha@123");
        UserRequestDTO request2 = new UserRequestDTO("Nathan Tanzi", "nathantanzi@test.com", "Senha@123");

        String responseBody = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserResponseDTO created = objectMapper.readValue(responseBody, UserResponseDTO.class);

        mockMvc.perform(put("/users/" + created.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nathan Tanzi"))
                .andExpect(jsonPath("$.email").value("nathantanzi@test.com"));
    }

    @Test
    void failWhenPutIdNotFoundTest() throws Exception {
        long nonExistentId = 999L;
        UserRequestDTO request = new UserRequestDTO("Nathan Tanzi", "nathantanzi@test.com", "Senha@123");

        mockMvc.perform(put("/users/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void failWhenPutWrongBodyTest() throws Exception {

        UserRequestDTO validRequest = new UserRequestDTO("Nathan", "nathan@test.com", "Senha@123");

        String responseBody = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserResponseDTO created = objectMapper.readValue(responseBody, UserResponseDTO.class);

        UserRequestDTO request1 = new UserRequestDTO(null, "nathantanzi@test.com", "Senha@123");
        UserRequestDTO request2 = new UserRequestDTO("Nathan Tanzi", null, "Senha@123");
        UserRequestDTO request3 = new UserRequestDTO("Nathan Tanzi", "nathantanzi@test.com", null);


        assertAll(
                () -> mockMvc.perform(put("/users/" + created.id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request1)))
                        .andExpect(status().isBadRequest()),

                () -> mockMvc.perform(put("/users/" + created.id())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request2)))
                        .andExpect(status().isBadRequest()),

                () ->
                        mockMvc.perform(put("/users/" + created.id())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(request3)))
                                .andExpect(status().isBadRequest())
        );
    }

    @Test
    void deleteSuccessfullyTest() throws Exception {
        UserRequestDTO request1 = new UserRequestDTO("Nathan", "nathan@test.com", "Senha@123");

        String responseBody = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserResponseDTO created = objectMapper.readValue(responseBody, UserResponseDTO.class);

        mockMvc.perform(delete("/users/" + created.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nathan"))
                .andExpect(jsonPath("$.email").value("nathan@test.com"));
    }

    @Test
    void failWhenDeleteIdNotFoundTest() throws Exception {
        long nonExistentId = 999L;

        mockMvc.perform(delete("/users/" + nonExistentId))
                .andExpect(status().isNotFound());
    }
}
