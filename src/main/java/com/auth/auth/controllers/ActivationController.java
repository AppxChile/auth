package com.auth.auth.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth.auth.entities.Usuario;
import com.auth.auth.repositories.UsuarioRepository;

@RestController
@RequestMapping("/api/auth/usuarios")
@CrossOrigin(origins = "https://dev.appx.cl/")
public class ActivationController {

    private final UsuarioRepository usuarioRepository;

    public ActivationController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/activate")
    public String activateAccount(@RequestParam String token) {
        Usuario usuario = usuarioRepository.findByActivationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

        usuario.setEnabled(true);
        usuario.setActivationToken(null);
        usuarioRepository.save(usuario);

        return "¡Cuenta activada correctamente!";
    }
}
