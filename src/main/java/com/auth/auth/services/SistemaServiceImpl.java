package com.auth.auth.services;


import org.springframework.stereotype.Service;

import com.auth.auth.entities.Sistema;
import com.auth.auth.exceptions.SistemaException;
import com.auth.auth.repositories.SistemaRepository;
import com.auth.auth.services.interfaces.SistemaService;

@Service
public class SistemaServiceImpl implements SistemaService {

    private final SistemaRepository sistemaRepository;

    public SistemaServiceImpl(SistemaRepository sistemaRepository) {
        this.sistemaRepository = sistemaRepository;
    }

    @Override
    public Sistema createSistema(String name, String code) {
        if (name == null || name.isBlank() || code == null || code.isBlank()) {
            throw new SistemaException("Nombre y código del sistema son obligatorios");
        }

        if (sistemaRepository.findByCodigo(code).isPresent()) {
            throw new SistemaException("El sistema ya existe");
        }

        return sistemaRepository.save(new Sistema(name, code));

    }

}
