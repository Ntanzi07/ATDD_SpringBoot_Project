package org.grupo1.grupo_1_praticaatdd.service;

import org.grupo1.grupo_1_praticaatdd.domain.Signature;
import org.grupo1.grupo_1_praticaatdd.domain.User;
import org.grupo1.grupo_1_praticaatdd.domain.enums.SignaturePlan;
import org.grupo1.grupo_1_praticaatdd.dto.SignatureRequestDTO;
import org.grupo1.grupo_1_praticaatdd.dto.SignatureResponseDTO;
import org.grupo1.grupo_1_praticaatdd.exception.SignatureNotFoundException;
import org.grupo1.grupo_1_praticaatdd.repository.SignatureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SignatureServiceTest {

    @Mock
    private SignatureRepository signatureRepository;

    @InjectMocks
    private SignatureService signatureService;

    @Test
    void getSignatureByIdTest() {
        User user = new User("Amanda", "amanda@test.com", "Hash@123456");
        Signature signature = user.getSignature();

        when(signatureRepository.findById(1L)).thenReturn(Optional.of(signature));

        SignatureResponseDTO response = signatureService.getSignatureById(1L);

        assertEquals(SignaturePlan.BASIC, response.plan());
        assertEquals(0, response.courseCredits());
        assertEquals(0, response.coins());
        verify(signatureRepository).findById(1L);
    }

    @Test
    void getAllSignaturesTest() {
        User user1 = new User("Amanda", "amanda@test.com", "Hash@123456");
        User user2 = new User("Marisol", "marisol@test.com", "Hash@654321");

        when(signatureRepository.findAll()).thenReturn(List.of(
                user1.getSignature(),
                user2.getSignature()
        ));

        List<SignatureResponseDTO> result = signatureService.getAllSignatures();

        assertEquals(2, result.size());
        verify(signatureRepository).findAll();
    }

    @Test
    void updateSignatureTest() {
        User user = new User("Amanda", "amanda@test.com", "Hash@123456");
        Signature signature = user.getSignature();

        SignatureRequestDTO request = new SignatureRequestDTO(
                SignaturePlan.PREMIUM, 10, 5, 100, null
        );

        when(signatureRepository.findById(1L)).thenReturn(Optional.of(signature));
        when(signatureRepository.save(signature)).thenReturn(signature);

        SignatureResponseDTO response = signatureService.updateSignature(1L, request);

        assertAll(
                () -> assertEquals(SignaturePlan.PREMIUM, response.plan()),
                () -> assertEquals(10, response.courseCredits()),
                () -> assertEquals(5, response.successFinishedCourses()),
                () -> assertEquals(100, response.coins())
        );
    }

    // teste pra ver se ele atualiza só o que mandou e nao mexe no resto
    @Test
    void updateParcialTest() {
        User user = new User("Amanda", "amanda@test.com", "Hash@123456");
        Signature sig = user.getSignature();

        SignatureRequestDTO request = new SignatureRequestDTO(
                SignaturePlan.PREMIUM, null, null, null, null
        );

        when(signatureRepository.findById(1L)).thenReturn(Optional.of(sig));
        when(signatureRepository.save(sig)).thenReturn(sig);

        SignatureResponseDTO response = signatureService.updateSignature(1L, request);

        assertEquals(SignaturePlan.PREMIUM, response.plan());
        // os outros campos tem que continuar iguais
        assertEquals(0, response.courseCredits());
        assertEquals(0, response.coins());
    }

    @Test
    void deleteSignatureTest() {
        User user = new User("Amanda", "amanda@test.com", "Hash@123456");
        Signature signature = user.getSignature();
        when(signatureRepository.findById(1L)).thenReturn(Optional.of(signature));

        SignatureResponseDTO response = signatureService.deleteSignature(1L);

        assertEquals(SignaturePlan.BASIC, response.plan());
        verify(signatureRepository).delete(signature);
    }

    @Test
    void failWhenNotFoundTest() {
        when(signatureRepository.findById(1L)).thenReturn(Optional.empty());

        assertAll(
                () -> assertThrows(SignatureNotFoundException.class,
                        () -> signatureService.getSignatureById(1L)),
                () -> assertThrows(SignatureNotFoundException.class,
                        () -> signatureService.updateSignature(1L,
                                new SignatureRequestDTO(SignaturePlan.PREMIUM, null, null, null, null))),
                () -> assertThrows(SignatureNotFoundException.class,
                        () -> signatureService.deleteSignature(1L))
        );
    }
}