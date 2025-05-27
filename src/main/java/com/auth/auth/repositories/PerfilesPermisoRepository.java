package com.auth.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.auth.entities.PerfilesPermiso;

public interface PerfilesPermisoRepository extends JpaRepository<PerfilesPermiso, Long> {

    PerfilesPermiso findByPerfilIdAndPermisoId(Long perfilId, Long permisoId);

}
