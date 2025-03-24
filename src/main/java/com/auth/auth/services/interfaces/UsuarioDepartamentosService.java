package com.auth.auth.services.interfaces;

import com.auth.auth.entities.Usuario;
import com.auth.auth.entities.UsuarioDepartamentos;

public interface UsuarioDepartamentosService {

    UsuarioDepartamentos findByUsuario(Usuario usuario);

    UsuarioDepartamentos save(UsuarioDepartamentos usuarioDepartamentos);

}
