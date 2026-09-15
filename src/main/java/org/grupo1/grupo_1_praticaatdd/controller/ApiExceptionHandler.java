package org.grupo1.grupo_1_praticaatdd.controller;


import org.grupo1.grupo_1_praticaatdd.exception.UserNotFoundException;
import org.grupo1.grupo_1_praticaatdd.exception.CourseNotFoundException;
import org.grupo1.grupo_1_praticaatdd.exception.SignatureNotFoundException;
import org.grupo1.grupo_1_praticaatdd.exception.RegistrationNumberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<String> handleCourseNotFound(CourseNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(SignatureNotFoundException.class)
    public ResponseEntity<String> handleSignatureNotFound(SignatureNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(RegistrationNumberNotFoundException.class)
    public ResponseEntity<String> handleRegistrationNumberNotFound(RegistrationNumberNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
