package org.grupo1.grupo_1_praticaatdd.service;

import org.grupo1.grupo_1_praticaatdd.domain.Course;
import org.grupo1.grupo_1_praticaatdd.domain.RegistrationNumber;
import org.grupo1.grupo_1_praticaatdd.domain.User;
import org.grupo1.grupo_1_praticaatdd.domain.enums.RegistrationNumberStatus;
import org.grupo1.grupo_1_praticaatdd.dto.ConcludeRegistrationNumberRequestDTO;
import org.grupo1.grupo_1_praticaatdd.dto.RegistrationNumberRequestDTO;
import org.grupo1.grupo_1_praticaatdd.dto.RegistrationNumberResponseDTO;
import org.grupo1.grupo_1_praticaatdd.exception.CourseNotFoundException;
import org.grupo1.grupo_1_praticaatdd.exception.RegistrationNumberNotFoundException;
import org.grupo1.grupo_1_praticaatdd.exception.UserNotFoundException;
import org.grupo1.grupo_1_praticaatdd.repository.CourseRepository;
import org.grupo1.grupo_1_praticaatdd.repository.RegistrationNumberRepository;
import org.grupo1.grupo_1_praticaatdd.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class RegistrationNumberService {

    private final RegistrationNumberRepository registrationNumberRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public RegistrationNumberService(RegistrationNumberRepository registrationNumberRepository,
                                     UserRepository userRepository,
                                     CourseRepository courseRepository) {
        this.registrationNumberRepository = registrationNumberRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public RegistrationNumberResponseDTO enroll(RegistrationNumberRequestDTO request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException(request.userId()));
        Course course = courseRepository.findById(request.courseId())
                .orElseThrow(() -> new CourseNotFoundException(request.courseId()));

        RegistrationNumber registrationNumber = new RegistrationNumber(user, course, request.bonus());
        return new RegistrationNumberResponseDTO(registrationNumberRepository.save(registrationNumber));
    }

    public RegistrationNumberResponseDTO getById(Long id) {
        return new RegistrationNumberResponseDTO(findById(id));
    }

    public RegistrationNumberResponseDTO conclude(Long id, ConcludeRegistrationNumberRequestDTO request) {
        if (request.finalGrade() == null || request.finalGrade() < 0 || request.finalGrade() > 10) {
            throw new IllegalArgumentException("Final grade must be between 0 and 10");
        }

        RegistrationNumber registrationNumber = findById(id);
        if (registrationNumber.getRegistrationNumberStatus() != RegistrationNumberStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Registration number is not in progress");
        }

        registrationNumber.setFinalGrade(request.finalGrade());
        registrationNumber.setRegistrationNumberStatus(RegistrationNumberStatus.COMPLETED);
        if (request.finalGrade() >= 7.0) {
            registrationNumber.getUser().getSignature().registerCourseCompletion();
        }

        return new RegistrationNumberResponseDTO(registrationNumberRepository.save(registrationNumber));
    }

    private RegistrationNumber findById(Long id) {
        return registrationNumberRepository.findById(id)
                .orElseThrow(() -> new RegistrationNumberNotFoundException(id));
    }
}