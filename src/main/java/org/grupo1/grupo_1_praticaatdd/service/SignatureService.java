package org.grupo1.grupo_1_praticaatdd.service;

import org.grupo1.grupo_1_praticaatdd.domain.RegistrationNumber;
import org.grupo1.grupo_1_praticaatdd.domain.Signature;
import org.grupo1.grupo_1_praticaatdd.domain.enums.RegistrationNumberStatus;
import org.grupo1.grupo_1_praticaatdd.dto.SignatureRequestDTO;
import org.grupo1.grupo_1_praticaatdd.dto.SignatureResponseDTO;
import org.grupo1.grupo_1_praticaatdd.exception.SignatureNotFoundException;
import org.grupo1.grupo_1_praticaatdd.repository.SignatureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignatureService {

    private final SignatureRepository signatureRepository;

    public SignatureService(SignatureRepository signatureRepository) {
        this.signatureRepository = signatureRepository;
    }

    public SignatureResponseDTO getSignatureById(Long id) {
        return new SignatureResponseDTO(findSignatureById(id));
    }

    public List<SignatureResponseDTO> getAllSignatures() {
        return signatureRepository.findAll().stream()
                .map(SignatureResponseDTO::new)
                .toList();
    }

    public SignatureResponseDTO updateSignature(Long id, SignatureRequestDTO request) {
        Signature signature = findSignatureById(id);

        if (request.plan() != null) {
            signature.setPlan(request.plan());
        }
        if (request.courseCredits() != null) {
            signature.setCourseCredits(request.courseCredits());
        }
        if (request.successFinishedCourses() != null) {
            signature.setSuccessFinishedCourses(request.successFinishedCourses());
        }
        if (request.coins() != null) {
            signature.setCoins(request.coins());
        }

        return new SignatureResponseDTO(signatureRepository.save(signature));
    }

    public SignatureResponseDTO deleteSignature(Long id) {
        Signature signature = findSignatureById(id);
        signatureRepository.delete(signature);
        return new SignatureResponseDTO(signature);
    }

    private Signature findSignatureById(Long id) {
        return signatureRepository.findById(id)
                .orElseThrow(() -> new SignatureNotFoundException(id));
    }
}