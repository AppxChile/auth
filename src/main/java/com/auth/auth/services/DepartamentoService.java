package com.auth.auth.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.auth.auth.entities.Departamento;
import com.auth.auth.repositories.DepartamentoRepository;

@Service
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    public List<Departamento> getAll() {

        return departamentoRepository.findAll();
    }

}
