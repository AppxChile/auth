package com.auth.auth.services.interfaces;

import com.auth.auth.dto.PermisoRequest;
import com.auth.auth.dto.PermisoResponse;

public interface PermisoService {

    PermisoResponse crearPermiso(PermisoRequest request);

}
