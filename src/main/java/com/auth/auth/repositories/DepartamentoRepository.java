package com.auth.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.auth.entities.Departamento;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

    Optional<Departamento> findById(Long id);

}
