package com.auth.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.auth.entities.Usuario;
import com.auth.auth.entities.UsuarioDepartamentos;

public interface UsuarioDepartamentosRepository extends JpaRepository<UsuarioDepartamentos, Long> {

    Optional<UsuarioDepartamentos> findByUsuario(Usuario usuario);

}
