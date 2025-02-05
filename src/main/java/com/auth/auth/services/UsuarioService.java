package com.auth.auth.services;

import java.util.List;

import com.auth.auth.dto.UsuarioResponse;
import com.auth.auth.entities.Usuario;

public interface UsuarioService {

      List<Usuario> findAll();

    UsuarioResponse save(Usuario usuario);


}
