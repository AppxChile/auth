package com.auth.auth.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.auth.auth.entities.Departamento;
import com.auth.auth.repositories.DepartamentoRepository;
import com.auth.auth.services.interfaces.DepartamentoService;

@Service
public class DepartamentoServiceImpl implements DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoServiceImpl(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Override
    public List<Departamento> getAll() {

        return departamentoRepository.findAll();
    }

    @Override
    public Departamento getById(Long id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El codigo de departamento no existe"));
    }

}
