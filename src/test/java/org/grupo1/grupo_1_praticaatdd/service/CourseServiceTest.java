package org.grupo1.grupo_1_praticaatdd.service;

import org.grupo1.grupo_1_praticaatdd.domain.Course;
import org.grupo1.grupo_1_praticaatdd.dto.CourseRequestDTO;
import org.grupo1.grupo_1_praticaatdd.dto.CourseResponseDTO;
import org.grupo1.grupo_1_praticaatdd.exception.CourseNotFoundException;
import org.grupo1.grupo_1_praticaatdd.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    void createCourseSuccessfullyTest() {
        CourseRequestDTO request = new CourseRequestDTO("Java", "Java course");
        Course savedCourse = new Course("Java", "Java course");
        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);

        CourseResponseDTO response = courseService.createCourse(request);

        assertAll(
                () -> assertEquals("Java", response.title()),
                () -> assertEquals("Java course", response.description())
        );
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void getCourseByIdSuccessfullyTest() {
        Course course = new Course("Java", "Java course");
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        CourseResponseDTO response = courseService.getCourseById(1L);

        assertEquals("Java", response.title());
        verify(courseRepository).findById(1L);
    }

    @Test
    void getAllCoursesSuccessfullyTest() {
        when(courseRepository.findAll()).thenReturn(List.of(
                new Course("Java", "Java course"),
                new Course("DevOps", "DevOps course")
        ));

        List<CourseResponseDTO> response = courseService.getAllCourses();

        assertAll(
                () -> assertEquals(2, response.size()),
                () -> assertEquals("Java", response.get(0).title()),
                () -> assertEquals("DevOps", response.get(1).title())
        );
        verify(courseRepository).findAll();
    }

    @Test
    void updateCourseSuccessfullyTest() {
        Course course = new Course("Java", "Java course");
        CourseRequestDTO request = new CourseRequestDTO("Advanced Java", "Advanced course");
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(course)).thenReturn(course);

        CourseResponseDTO response = courseService.updateCourse(1L, request);

        assertAll(
                () -> assertEquals("Advanced Java", response.title()),
                () -> assertEquals("Advanced course", response.description())
        );
        verify(courseRepository).save(course);
    }

    @Test
    void deleteCourseSuccessfullyTest() {
        Course course = new Course("Java", "Java course");
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        CourseResponseDTO response = courseService.deleteCourse(1L);

        assertEquals("Java", response.title());
        verify(courseRepository).delete(course);
    }

    @Test
    void failWhenCourseIsNotFoundTest() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertAll(
                () -> assertThrows(CourseNotFoundException.class, () -> courseService.getCourseById(1L)),
                () -> assertThrows(CourseNotFoundException.class, () -> courseService.updateCourse(1L,
                        new CourseRequestDTO("Java", "Java course"))),
                () -> assertThrows(CourseNotFoundException.class, () -> courseService.deleteCourse(1L))
        );
    }
}