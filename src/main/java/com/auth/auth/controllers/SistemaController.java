package com.auth.auth.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.auth.dto.SistemaRequest;
import com.auth.auth.exceptions.SistemaException;
import com.auth.auth.services.interfaces.SistemaService;

@RestController
@RequestMapping("/api/auth/usuarios/sistemas")
@CrossOrigin(origins = "http://localhost:5713")
public class SistemaController {

    private final SistemaService sistemaService;

    public SistemaController(SistemaService sistemaService) {
        this.sistemaService = sistemaService;
    }

    @PostMapping
    public ResponseEntity<Object> createSistema(@RequestBody SistemaRequest request){
        try {
            return ResponseEntity.ok(sistemaService.createSistema(request.getNombreSistema(), request.getCodigoSistema()));
        } catch (SistemaException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

}
