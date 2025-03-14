package com.auth.auth.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.auth.dto.ChangeMailRequest;
import com.auth.auth.services.UsuarioService;

@RestController
@RequestMapping("/api/auth/usuarios/")
@CrossOrigin(origins = { "https://dev.appx.cl/", "http//localhost:5173" })
public class EmailController {

    private final UsuarioService usuarioService;

    public EmailController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/change-mail")
    public ResponseEntity<Object> changeMail(@RequestBody ChangeMailRequest request) {

        try {
            usuarioService.changeMail(request);
            return ResponseEntity.ok("correo enviado");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("error al cambiar el maio" + e.getMessage());
        }

    }

}
