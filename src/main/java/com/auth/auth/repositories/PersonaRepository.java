package com.auth.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.auth.entities.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

    Optional<Persona> findByRut(Integer rut);

}
