package org.grupo1.grupo_1_praticaatdd.repository;

import org.grupo1.grupo_1_praticaatdd.domain.Signature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignatureRepository extends JpaRepository<Signature, Long> {
}