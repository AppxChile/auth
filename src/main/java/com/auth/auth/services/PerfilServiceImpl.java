package com.auth.auth.services;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import com.auth.auth.dto.PerfilRequest;
import com.auth.auth.dto.PerfilResponse;
import com.auth.auth.entities.Perfil;
import com.auth.auth.entities.Sistema;
import com.auth.auth.repositories.PerfilRepository;
import com.auth.auth.repositories.SistemaRepository;
import com.auth.auth.services.interfaces.PerfilService;

public class PerfilServiceImpl implements PerfilService {

    private final PerfilRepository perfilRepository;

    private final SistemaRepository sistemaRepository;

    public PerfilServiceImpl(PerfilRepository perfilRepository, SistemaRepository sistemaRepository) {
        this.perfilRepository = perfilRepository;
        this.sistemaRepository = sistemaRepository;
    }

    @Override
    public PerfilResponse crearPerfil(PerfilRequest request) {
        Perfil perfil = new Perfil();
        perfil.setNombre(request.getNombre());

        Set<Sistema> sistemas = new HashSet<>();
        for (Long sistemaId : request.getSistemaIds()) {
            Sistema sistema = sistemaRepository.findById(sistemaId)
                    .orElseThrow(() -> new NoSuchElementException("Sistema no encontrado con ID: " + sistemaId));
            sistemas.add(sistema);
        }

        perfil.setSistemas(sistemas);
        Perfil guardado = perfilRepository.save(perfil);

        Set<String> nombresSistemas = new HashSet<>();
        for (Sistema sistema : guardado.getSistemas()) {
            nombresSistemas.add(sistema.getNombre());
        }

        return new PerfilResponse(guardado.getId(), guardado.getNombre(), nombresSistemas);
    }
}