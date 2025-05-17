package com.auth.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.auth.entities.Permiso;
import com.auth.auth.entities.Sistema;

public interface PermisoRepository extends JpaRepository<Permiso, Long> {

     Optional<Permiso> findByNombreAndSistema(String nombre, Sistema sistema);

}
