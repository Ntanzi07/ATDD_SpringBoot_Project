package org.grupo1.grupo_1_praticaatdd.repository;

import org.grupo1.grupo_1_praticaatdd.domain.RegistrationNumber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationNumberRepository extends JpaRepository<RegistrationNumber, Long> {
}