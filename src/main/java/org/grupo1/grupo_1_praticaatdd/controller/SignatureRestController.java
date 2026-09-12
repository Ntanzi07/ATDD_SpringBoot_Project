package org.grupo1.grupo_1_praticaatdd.controller;

import org.grupo1.grupo_1_praticaatdd.dto.SignatureRequestDTO;
import org.grupo1.grupo_1_praticaatdd.dto.SignatureResponseDTO;
import org.grupo1.grupo_1_praticaatdd.service.SignatureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/signatures")
public class SignatureRestController {

    private final SignatureService signatureService;

    public SignatureRestController(SignatureService signatureService) {
        this.signatureService = signatureService;
    }

    @GetMapping
    public ResponseEntity<List<SignatureResponseDTO>> getAllSignatures() {
        List<SignatureResponseDTO> response = signatureService.getAllSignatures();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SignatureResponseDTO> getSignatureById(@PathVariable Long id) {
        SignatureResponseDTO response = signatureService.getSignatureById(id);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SignatureResponseDTO> updateSignature(@PathVariable Long id, @RequestBody SignatureRequestDTO request) {
        SignatureResponseDTO response = signatureService.updateSignature(id, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SignatureResponseDTO> deleteSignature(@PathVariable Long id) {
        SignatureResponseDTO response = signatureService.deleteSignature(id);
        return ResponseEntity.ok().body(response);
    }
}