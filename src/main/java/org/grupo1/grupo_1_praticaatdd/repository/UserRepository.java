package org.grupo1.grupo_1_praticaatdd.repository;

import org.grupo1.grupo_1_praticaatdd.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
