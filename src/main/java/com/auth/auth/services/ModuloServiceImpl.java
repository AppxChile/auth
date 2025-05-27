package com.auth.auth.services;

import com.auth.auth.dto.ModuloRequest;
import com.auth.auth.dto.ModuloResponse;
import com.auth.auth.entities.Modulo;
import com.auth.auth.entities.Sistema;
import com.auth.auth.repositories.ModuloRepository;
import com.auth.auth.repositories.SistemaRepository;
import com.auth.auth.services.interfaces.ModuloService;

public class ModuloServiceImpl implements ModuloService {

    private final ModuloRepository moduloRepository;
    private final SistemaRepository sistemaRepository;

    public ModuloServiceImpl(ModuloRepository moduloRepository, SistemaRepository sistemaRepository) {
        this.moduloRepository = moduloRepository;
        this.sistemaRepository = sistemaRepository;
    }

    @Override
    public ModuloResponse crearModulo(ModuloRequest request) {
        Sistema sistema = sistemaRepository.findById(request.getSistemaId())
                .orElseThrow(() -> new IllegalArgumentException("Sistema no encontrado"));

        Modulo modulo = new Modulo();
        modulo.setCodigo(request.getCodigo());
        modulo.setNombre(request.getNombre());
        modulo.setSistema(sistema);

        Modulo guardado = moduloRepository.save(modulo);

        return new ModuloResponse(
                guardado.getId(),
                guardado.getCodigo(),
                guardado.getNombre(),
                guardado.getSistema().getNombre());
    }
}
