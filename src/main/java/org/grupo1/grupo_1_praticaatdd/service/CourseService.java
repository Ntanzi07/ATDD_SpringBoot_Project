package org.grupo1.grupo_1_praticaatdd.service;

import org.grupo1.grupo_1_praticaatdd.domain.Course;
import org.grupo1.grupo_1_praticaatdd.dto.CourseRequestDTO;
import org.grupo1.grupo_1_praticaatdd.dto.CourseResponseDTO;
import org.grupo1.grupo_1_praticaatdd.exception.CourseNotFoundException;
import org.grupo1.grupo_1_praticaatdd.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public CourseResponseDTO createCourse(CourseRequestDTO request) {
        Course course = new Course(request.title(), request.description());
        return new CourseResponseDTO(courseRepository.save(course));
    }

    public CourseResponseDTO getCourseById(Long id) {
        return new CourseResponseDTO(findCourseById(id));
    }

    public List<CourseResponseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(CourseResponseDTO::new)
                .toList();
    }

    public CourseResponseDTO updateCourse(Long id, CourseRequestDTO request) {
        Course course = findCourseById(id);
        course.changeTitle(request.title());
        course.changeDescription(request.description());
        return new CourseResponseDTO(courseRepository.save(course));
    }

    public CourseResponseDTO deleteCourse(Long id) {
        Course course = findCourseById(id);
        courseRepository.delete(course);
        return new CourseResponseDTO(course);
    }

    private Course findCourseById(Long id) {
        return courseRepository.findById(id)
            .orElseThrow(() -> new CourseNotFoundException(id));
    }
}