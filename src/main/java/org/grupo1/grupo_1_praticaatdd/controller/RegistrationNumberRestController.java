package org.grupo1.grupo_1_praticaatdd.controller;

import org.grupo1.grupo_1_praticaatdd.dto.ConcludeRegistrationNumberRequestDTO;
import org.grupo1.grupo_1_praticaatdd.dto.RegistrationNumberRequestDTO;
import org.grupo1.grupo_1_praticaatdd.dto.RegistrationNumberResponseDTO;
import org.grupo1.grupo_1_praticaatdd.service.RegistrationNumberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration-numbers")
public class RegistrationNumberRestController {

    private final RegistrationNumberService registrationNumberService;

    public RegistrationNumberRestController(RegistrationNumberService registrationNumberService) {
        this.registrationNumberService = registrationNumberService;
    }

    @PostMapping
    public ResponseEntity<RegistrationNumberResponseDTO> enroll(@RequestBody RegistrationNumberRequestDTO request) {
        return ResponseEntity.status(201).body(registrationNumberService.enroll(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistrationNumberResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(registrationNumberService.getById(id));
    }

    @PatchMapping("/{id}/conclude")
    public ResponseEntity<RegistrationNumberResponseDTO> conclude(
            @PathVariable Long id,
            @RequestBody ConcludeRegistrationNumberRequestDTO request) {
        return ResponseEntity.ok(registrationNumberService.conclude(id, request));
    }
}