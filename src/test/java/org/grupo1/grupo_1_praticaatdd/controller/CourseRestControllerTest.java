package org.grupo1.grupo_1_praticaatdd.controller;

import org.grupo1.grupo_1_praticaatdd.domain.Course;
import org.grupo1.grupo_1_praticaatdd.dto.CourseRequestDTO;
import org.grupo1.grupo_1_praticaatdd.dto.CourseResponseDTO;
import org.grupo1.grupo_1_praticaatdd.repository.CourseRepository;
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
class CourseRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void cleanDatabase() {
        courseRepository.deleteAll();
    }

    @Test
    void createCourseSuccessfullyTest() throws Exception {
        CourseRequestDTO request = new CourseRequestDTO("Java", "Java course");

        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Java"))
                .andExpect(jsonPath("$.description").value("Java course"));
    }

    @Test
    void getAllCoursesSuccessfullyTest() throws Exception {
        courseRepository.save(new Course("Java", "Java course"));
        courseRepository.save(new Course("DevOps", "DevOps course"));

        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getCourseByIdSuccessfullyTest() throws Exception {
        Course course = courseRepository.save(new Course("Java", "Java course"));

        mockMvc.perform(get("/courses/" + course.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Java"))
                .andExpect(jsonPath("$.description").value("Java course"));
    }

    @Test
    void updateCourseSuccessfullyTest() throws Exception {
        Course course = courseRepository.save(new Course("Java", "Java course"));
        CourseRequestDTO request = new CourseRequestDTO("Advanced Java", "Advanced course");

        mockMvc.perform(put("/courses/" + course.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Advanced Java"))
                .andExpect(jsonPath("$.description").value("Advanced course"));
    }

    @Test
    void deleteCourseSuccessfullyTest() throws Exception {
        Course course = courseRepository.save(new Course("Java", "Java course"));

        mockMvc.perform(delete("/courses/" + course.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Java"));

        assertEquals(0, courseRepository.count());
    }

    @Test
    void failWhenCourseIsNotFoundTest() throws Exception {
        mockMvc.perform(get("/courses/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void failWhenCourseTitleIsInvalidTest() throws Exception {
        CourseRequestDTO request = new CourseRequestDTO("", "Java course");

        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}