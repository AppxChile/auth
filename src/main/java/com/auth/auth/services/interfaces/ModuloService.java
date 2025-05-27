package com.auth.auth.services.interfaces;

import com.auth.auth.dto.ModuloRequest;
import com.auth.auth.dto.ModuloResponse;

public interface ModuloService {

    ModuloResponse crearModulo(ModuloRequest request);

}
