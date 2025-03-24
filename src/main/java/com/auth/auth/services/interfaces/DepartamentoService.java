package com.auth.auth.services.interfaces;

import java.util.List;

import com.auth.auth.entities.Departamento;

public interface DepartamentoService {

     List<Departamento> getAll();

     Departamento getById(Long id);

}
