package org.grupo1.grupo_1_praticaatdd.controller;

import org.grupo1.grupo_1_praticaatdd.domain.Course;
import org.grupo1.grupo_1_praticaatdd.domain.User;
import org.grupo1.grupo_1_praticaatdd.dto.ConcludeRegistrationNumberRequestDTO;
import org.grupo1.grupo_1_praticaatdd.dto.RegistrationNumberRequestDTO;
import org.grupo1.grupo_1_praticaatdd.repository.CourseRepository;
import org.grupo1.grupo_1_praticaatdd.repository.RegistrationNumberRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RegistrationNumberRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private RegistrationNumberRepository registrationNumberRepository;

    @BeforeEach
    void cleanDatabase() {
        registrationNumberRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void enrollsUserAndReturnsInProgressRegistration() throws Exception {
        User user = userRepository.save(new User("Amanda", "amanda@test.com", "Hash@123456"));
        Course course = courseRepository.save(new Course("Java", "Java course"));
        RegistrationNumberRequestDTO request = new RegistrationNumberRequestDTO(user.getId(), course.getId(), false);

        mockMvc.perform(post("/registration-numbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.courseId").value(course.getId()))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void concludesRegistrationAndUpdatesStatus() throws Exception {
        User user = userRepository.save(new User("Amanda", "amanda@test.com", "Hash@123456"));
        Course course = courseRepository.save(new Course("Java", "Java course"));
        RegistrationNumberRequestDTO enrollment = new RegistrationNumberRequestDTO(user.getId(), course.getId(), false);

        String response = mockMvc.perform(post("/registration-numbers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enrollment)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        long registrationId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(patch("/registration-numbers/" + registrationId + "/conclude")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ConcludeRegistrationNumberRequestDTO(8.5))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.finalGrade").value(8.5));
    }

    @Test
    void returnsNotFoundForUnknownRegistration() throws Exception {
        mockMvc.perform(patch("/registration-numbers/999/conclude")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ConcludeRegistrationNumberRequestDTO(8.5))))
                .andExpect(status().isNotFound());
    }
}