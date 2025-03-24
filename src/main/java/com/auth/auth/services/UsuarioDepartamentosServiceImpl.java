package com.auth.auth.services;

import org.springframework.stereotype.Service;

import com.auth.auth.entities.Usuario;
import com.auth.auth.entities.UsuarioDepartamentos;
import com.auth.auth.repositories.UsuarioDepartamentosRepository;
import com.auth.auth.services.interfaces.UsuarioDepartamentosService;

@Service
public class UsuarioDepartamentosServiceImpl implements UsuarioDepartamentosService {

    private final UsuarioDepartamentosRepository usuarioDepartamentosRepository;

    public UsuarioDepartamentosServiceImpl(UsuarioDepartamentosRepository usuarioDepartamentosRepository) {
        this.usuarioDepartamentosRepository = usuarioDepartamentosRepository;
    }

    @Override
    public UsuarioDepartamentos findByUsuario(Usuario usuario) {

        return usuarioDepartamentosRepository.findByUsuario(usuario)
                .orElse(null);

    }

    @Override
    public UsuarioDepartamentos save(UsuarioDepartamentos usuarioDepartamentos) {
        return usuarioDepartamentosRepository.save(usuarioDepartamentos);
    }

}
