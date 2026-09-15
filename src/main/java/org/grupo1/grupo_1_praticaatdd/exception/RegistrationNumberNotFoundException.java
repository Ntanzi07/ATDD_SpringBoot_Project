package org.grupo1.grupo_1_praticaatdd.exception;

public class RegistrationNumberNotFoundException extends RuntimeException {

    public RegistrationNumberNotFoundException(Long id) {
        super("Registration number not found with id: " + id);
    }
}