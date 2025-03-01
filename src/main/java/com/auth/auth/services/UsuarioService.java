package com.auth.auth.services;

import java.util.List;

import com.auth.auth.dto.ChangeMailRequest;
import com.auth.auth.dto.UsuarioRequest;
import com.auth.auth.dto.UsuarioResponse;
import com.auth.auth.entities.Usuario;

public interface UsuarioService {

  List<Usuario> findAll();

  UsuarioResponse save(Usuario usuario);

  UsuarioResponse saveUserFunc(UsuarioRequest usuario);

  UsuarioResponse buscarUsuario(String username);

  void changeMail(ChangeMailRequest request);

}
