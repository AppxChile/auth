package com.auth.auth.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.auth.entities.Rol;
import com.auth.auth.entities.Usuario;
import com.auth.auth.repositories.RolRepository;
import com.auth.auth.repositories.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final RolRepository rolRepository;

    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    

    @Override
    public Usuario save(Usuario usuario) {
        Optional<Rol> optRol = rolRepository.findByName("ROLE_USER");
        List<Rol> roles = new ArrayList<>();

        optRol.ifPresent(roles::add);

        if (usuario.isAdmin()) {
            Optional<Rol> optRolAdmin = rolRepository.findByName("ROLE_ADMIN");
            optRolAdmin.ifPresent(roles::add);
        }

        usuario.setRoles(roles);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setEnabled(true);


        return usuarioRepository.save(usuario);
    }

}


