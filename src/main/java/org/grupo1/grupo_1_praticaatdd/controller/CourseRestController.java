package org.grupo1.grupo_1_praticaatdd.controller;

import org.grupo1.grupo_1_praticaatdd.dto.CourseRequestDTO;
import org.grupo1.grupo_1_praticaatdd.dto.CourseResponseDTO;
import org.grupo1.grupo_1_praticaatdd.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseRestController {
    private final CourseService courseService;

    public CourseRestController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseResponseDTO> createCourse(@RequestBody CourseRequestDTO request) {
        return ResponseEntity.status(201).body(courseService.createCourse(request));
    }

    @GetMapping
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable Long id,
                                                           @RequestBody CourseRequestDTO request) {
        return ResponseEntity.ok(courseService.updateCourse(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> deleteCourse(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.deleteCourse(id));
    }
}