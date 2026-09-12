package org.grupo1.grupo_1_praticaatdd.exception;

public class SignatureNotFoundException extends RuntimeException {
    public SignatureNotFoundException(Long id) {
        super("Signature not found with id: " + id);
    }
}