package com.auth.auth.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.auth.auth.dto.SistemaRequest;
import com.auth.auth.dto.SistemaResponse;
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
    public SistemaResponse createSistema(SistemaRequest request) {
        if (sistemaRepository.existsByCodigo(request.getCodigoSistema())) {
            throw new SistemaException("Ya existe un sistema con el código: " + request.getCodigoSistema());
        }

        Sistema sistema = new Sistema();
        sistema.setNombre(request.getNombreSistema().toUpperCase());
        sistema.setCodigo(request.getCodigoSistema().toUpperCase());

        sistema = sistemaRepository.save(sistema);
        return new SistemaResponse(sistema.getId(), sistema.getNombre(), sistema.getCodigo());
    }

    @Override
    public void eliminarSistema(Long id) {
        sistemaRepository.deleteById(id);
    }

    @Override
    public Optional<Sistema> buscarPorId(Long id) {
        return sistemaRepository.findById(id);
    }

    @Override
    public List<SistemaResponse> listarTodos() {
        return sistemaRepository.findAll().stream()
                .map(s -> new SistemaResponse(s.getId(), s.getNombre(), s.getCodigo()))
                .toList();
    }

}
