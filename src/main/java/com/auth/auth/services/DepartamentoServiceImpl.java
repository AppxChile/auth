package com.auth.auth.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.auth.auth.entities.Departamento;
import com.auth.auth.repositories.DepartamentoRepository;
import com.auth.auth.services.interfaces.DepartamentoService;
import com.auth.auth.utils.RepositoryUtils;

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

        return RepositoryUtils.findOrThrow(departamentoRepository.findById(id),
                String.format("No se encontró el departamento con el id %d", id));
    }

}
