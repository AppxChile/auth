package com.auth.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.auth.entities.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByName(String name);

}
