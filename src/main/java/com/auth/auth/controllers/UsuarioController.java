package com.auth.auth.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.auth.dto.UsuarioRequest;
import com.auth.auth.dto.UsuarioResponse;
import com.auth.auth.entities.Usuario;
import com.auth.auth.services.UsuarioService;

@RestController
@RequestMapping("/auth/api/usuarios")
@CrossOrigin(origins = "http://localhost:5173")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }


    @GetMapping("/list")
    public List<Usuario> list(){
        return usuarioService.findAll();
    }

    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody Usuario usuario){

        try {
            UsuarioResponse newUsuario = usuarioService.save(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUsuario);


        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        
    }

    @PostMapping("/create-func")
    public ResponseEntity<Object> createFunc(@RequestBody UsuarioRequest usuario){

        try {
            UsuarioResponse newUsuario = usuarioService.saveUserFunc(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUsuario);


        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        
    }


    

}