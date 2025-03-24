package com.auth.auth.services;

import java.util.List;

import com.auth.auth.dto.ChangeMailRequest;
import com.auth.auth.dto.UsuarioRequest;
import com.auth.auth.dto.UsuarioResponse;
import com.auth.auth.dto.UsuarioResponseList;
import com.auth.auth.entities.Persona;
import com.auth.auth.entities.Usuario;

public interface UsuarioService {

  List<UsuarioResponseList> findAll();

  UsuarioResponse createUser(Usuario usuario);

  UsuarioResponse createUserFunc(UsuarioRequest usuario);

  UsuarioResponse getUsuario(String username);

  void changeMail(ChangeMailRequest request);

  Usuario getUsuarioByPersona(Persona persona);

  Usuario save(Usuario usuario);

  Usuario findByUsername(String username);

}
