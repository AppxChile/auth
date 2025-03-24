package com.auth.auth.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.auth.auth.entities.Rol;
import com.auth.auth.repositories.RolRepository;
import com.auth.auth.services.interfaces.RolService;

@Service
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    public RolServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public Optional<Rol> getByName(String name) {
        return rolRepository.findByName(name);
    }

}
