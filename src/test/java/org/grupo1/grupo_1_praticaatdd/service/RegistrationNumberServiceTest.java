package org.grupo1.grupo_1_praticaatdd.service;

import org.grupo1.grupo_1_praticaatdd.domain.Course;
import org.grupo1.grupo_1_praticaatdd.domain.RegistrationNumber;
import org.grupo1.grupo_1_praticaatdd.domain.User;
import org.grupo1.grupo_1_praticaatdd.domain.enums.RegistrationNumberStatus;
import org.grupo1.grupo_1_praticaatdd.domain.enums.SignaturePlan;
import org.grupo1.grupo_1_praticaatdd.dto.ConcludeRegistrationNumberRequestDTO;
import org.grupo1.grupo_1_praticaatdd.dto.RegistrationNumberRequestDTO;
import org.grupo1.grupo_1_praticaatdd.dto.RegistrationNumberResponseDTO;
import org.grupo1.grupo_1_praticaatdd.exception.CourseNotFoundException;
import org.grupo1.grupo_1_praticaatdd.exception.UserNotFoundException;
import org.grupo1.grupo_1_praticaatdd.repository.CourseRepository;
import org.grupo1.grupo_1_praticaatdd.repository.RegistrationNumberRepository;
import org.grupo1.grupo_1_praticaatdd.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationNumberServiceTest {

    @Mock
    private RegistrationNumberRepository registrationNumberRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private RegistrationNumberService registrationNumberService;

    @Test
    void enrollsUserInCourseWithInProgressStatus() {
        User user = new User("Amanda", "amanda@test.com", "Hash@123456");
        Course course = new Course("Java", "Java course");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(course));
        when(registrationNumberRepository.save(any(RegistrationNumber.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RegistrationNumberResponseDTO response = registrationNumberService.enroll(
                new RegistrationNumberRequestDTO(1L, 2L, true));

        assertEquals(RegistrationNumberStatus.IN_PROGRESS, response.status());
        assertEquals(true, response.bonus());
        verify(registrationNumberRepository).save(any(RegistrationNumber.class));
    }

    @Test
    void concludesWithPassingGradeAndPromotesSignatureAfterTwelveCourses() {
        User user = new User("Amanda", "amanda@test.com", "Hash@123456");
        user.getSignature().setSuccessFinishedCourses(11);
        Course course = new Course("Java", "Java course");
        RegistrationNumber registrationNumber = new RegistrationNumber(user, course, false);
        when(registrationNumberRepository.findById(1L)).thenReturn(Optional.of(registrationNumber));
        when(registrationNumberRepository.save(registrationNumber)).thenReturn(registrationNumber);

        RegistrationNumberResponseDTO response = registrationNumberService.conclude(
                1L, new ConcludeRegistrationNumberRequestDTO(7.0));

        assertEquals(RegistrationNumberStatus.COMPLETED, response.status());
        assertEquals(7.0, response.finalGrade());
        assertEquals(12, user.getSignature().getSuccessFinishedCourses());
        assertEquals(SignaturePlan.PREMIUM, user.getSignature().getPlan());
    }

    @Test
    void concludesWithFailingGradeWithoutCountingCompletedCourse() {
        User user = new User("Amanda", "amanda@test.com", "Hash@123456");
        Course course = new Course("Java", "Java course");
        RegistrationNumber registrationNumber = new RegistrationNumber(user, course, false);
        when(registrationNumberRepository.findById(1L)).thenReturn(Optional.of(registrationNumber));
        when(registrationNumberRepository.save(registrationNumber)).thenReturn(registrationNumber);

        registrationNumberService.conclude(1L, new ConcludeRegistrationNumberRequestDTO(6.9));

        assertEquals(RegistrationNumberStatus.COMPLETED, registrationNumber.getRegistrationNumberStatus());
        assertEquals(0, user.getSignature().getSuccessFinishedCourses());
    }

    @Test
    void rejectsMissingUserOrCourse() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> registrationNumberService.enroll(
                new RegistrationNumberRequestDTO(1L, 2L, false)));

        User user = new User("Amanda", "amanda@test.com", "Hash@123456");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(courseRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(CourseNotFoundException.class, () -> registrationNumberService.enroll(
                new RegistrationNumberRequestDTO(1L, 2L, false)));
    }
}