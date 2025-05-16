package com.auth.auth.services;

import org.springframework.stereotype.Service;

import com.auth.auth.entities.Permiso;
import com.auth.auth.entities.Sistema;
import com.auth.auth.exceptions.PermisoException;
import com.auth.auth.repositories.PermisoRepository;
import com.auth.auth.repositories.SistemaRepository;
import com.auth.auth.services.interfaces.PermisoService;

@Service
public class PermisoServiceImpl implements PermisoService {

    private final PermisoRepository permisoRepository;

    private final SistemaRepository sistemaRepository;

    public PermisoServiceImpl(PermisoRepository permisoRepository, SistemaRepository sistemaRepository) {
        this.permisoRepository = permisoRepository;
        this.sistemaRepository = sistemaRepository;
    }

    @Override
    public Permiso createPermiso(String name, String codeSitema) {
        if (name == null || name.isBlank() || codeSitema == null || codeSitema.isBlank()) {
            throw new PermisoException("Nombre y código del sistema son obligatorios");
        }

        Sistema sistema = sistemaRepository.findByCodigo(codeSitema)
                .orElseThrow(() -> new PermisoException("Sistema no encontrado"));

        if (permisoRepository.findByNombreAndSistema(name, sistema).isPresent()) {
            throw new PermisoException("El permiso ya existe para este sistema");
        }

        return permisoRepository.save(new Permiso(name, sistema));
    }
}
