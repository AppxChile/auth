package com.auth.auth.services;

import com.auth.auth.entities.Perfil;
import com.auth.auth.exceptions.PerfilException;
import com.auth.auth.repositories.PerfilRepository;
import com.auth.auth.services.interfaces.PerfilService;

public class PerfilServiceImpl implements PerfilService {


    private final PerfilRepository perfilRepository;

    public PerfilServiceImpl(PerfilRepository perfilRepository) {
        this.perfilRepository = perfilRepository;
    }

    @Override
    public Perfil createPerfil(String nombre) {
        if (perfilRepository.findByNombre(nombre).isPresent()) {
            throw new PerfilException("El perfil ya existe");
        }
        Perfil perfil = new Perfil();
        perfil.setNombre(nombre);
        return perfilRepository.save(perfil);
}
}