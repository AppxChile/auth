package com.auth.auth.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.auth.dto.UsuarioRequest;
import com.auth.auth.dto.UsuarioResponse;
import com.auth.auth.dto.UsuarioResponseList;
import com.auth.auth.entities.Usuario;
import com.auth.auth.services.UsuarioService;

@RestController
@RequestMapping("/api/auth/usuarios")
@CrossOrigin(origins = { "https://dev.appx.cl/", "http//localhost:5173" })
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getList() {
        try {

            List<UsuarioResponseList> usuariosList = usuarioService.findAll();
            return ResponseEntity.ok(usuariosList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody Usuario usuario) {

        try {
            UsuarioResponse newUsuario = usuarioService.createUser(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUsuario);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @PostMapping("/create-func")
    public ResponseEntity<Object> createFunc(@RequestBody UsuarioRequest usuario) {

        try {
            UsuarioResponse newUsuario = usuarioService.createUserFunc(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUsuario);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping("/buscar/{username}")
    public ResponseEntity<Object> getUsuario(@PathVariable String username) {
        try {
            UsuarioResponse usuarioResponse = usuarioService.getUsuario(username);
            return ResponseEntity.ok().body(usuarioResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

}