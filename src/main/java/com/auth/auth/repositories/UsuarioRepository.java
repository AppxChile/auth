package com.auth.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.auth.entities.Persona;
import com.auth.auth.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByActivationToken(String activationToken);

    Optional<Usuario> findByPersona(Persona persona);

}
