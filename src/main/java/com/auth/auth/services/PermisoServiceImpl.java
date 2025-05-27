package com.auth.auth.services;

import org.springframework.stereotype.Service;

import com.auth.auth.dto.PermisoRequest;
import com.auth.auth.dto.PermisoResponse;
import com.auth.auth.entities.Modulo;
import com.auth.auth.entities.Permiso;
import com.auth.auth.entities.Sistema;
import com.auth.auth.repositories.ModuloRepository;
import com.auth.auth.repositories.PermisoRepository;
import com.auth.auth.repositories.SistemaRepository;
import com.auth.auth.services.interfaces.PermisoService;

@Service
public class PermisoServiceImpl implements PermisoService {

    private final PermisoRepository permisoRepository;

    private final SistemaRepository sistemaRepository;

    private final ModuloRepository moduloRepository;

    public PermisoServiceImpl(PermisoRepository permisoRepository, SistemaRepository sistemaRepository,
            ModuloRepository moduloRepository) {
        this.permisoRepository = permisoRepository;
        this.sistemaRepository = sistemaRepository;
        this.moduloRepository = moduloRepository;
    }

    @Override
    public PermisoResponse crearPermiso(PermisoRequest request) {
        Sistema sistema = sistemaRepository.findById(request.getSistemaId())
                .orElseThrow(() -> new IllegalArgumentException("Sistema no encontrado"));

        Modulo modulo = moduloRepository.findById(request.getModuloId())
                .orElseThrow(() -> new IllegalArgumentException("Módulo no encontrado"));

        Permiso permiso = new Permiso();
        permiso.setNombre(request.getNombre());
        permiso.setSistema(sistema);
        permiso.setModulo(modulo);

        Permiso guardado = permisoRepository.save(permiso);

        return new PermisoResponse(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getSistema().getCodigo(),
                guardado.getModulo().getCodigo());
    }
}
