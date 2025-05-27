package com.auth.auth.services.interfaces;

import com.auth.auth.dto.PerfilRequest;
import com.auth.auth.dto.PerfilResponse;

public interface PerfilService {

       PerfilResponse crearPerfil(PerfilRequest request);

}
