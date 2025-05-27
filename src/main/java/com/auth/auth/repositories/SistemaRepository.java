package com.auth.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.auth.entities.Sistema;

public interface SistemaRepository extends JpaRepository<Sistema, Long> {

    Optional<Sistema> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

}
