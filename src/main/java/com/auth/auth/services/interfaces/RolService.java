package com.auth.auth.services.interfaces;

import java.util.Optional;

import com.auth.auth.entities.Rol;

public interface RolService {

    Optional<Rol> getByName(String name);

}
