package com.auth.auth.services;

import java.util.List;

import com.auth.auth.entities.Usuario;

public interface UsuarioService {

      List<Usuario> findAll();

    Usuario save(Usuario usuario);


}
