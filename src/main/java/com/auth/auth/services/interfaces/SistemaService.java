package com.auth.auth.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.auth.auth.dto.SistemaRequest;
import com.auth.auth.dto.SistemaResponse;
import com.auth.auth.entities.Sistema;

public interface SistemaService {

    SistemaResponse createSistema(SistemaRequest request);

    void eliminarSistema(Long id);

    Optional<Sistema> buscarPorId(Long id);

    List<SistemaResponse> listarTodos();

}
