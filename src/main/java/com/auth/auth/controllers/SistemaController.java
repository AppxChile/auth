package com.auth.auth.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.auth.dto.SistemaRequest;
import com.auth.auth.dto.SistemaResponse;
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
     public ResponseEntity<SistemaResponse> crearSistema(@RequestBody SistemaRequest request) {
        SistemaResponse creado = sistemaService.createSistema(request);
        return ResponseEntity.ok(creado);
    }

     @GetMapping
    public ResponseEntity<List<SistemaResponse>> listar() {
        return ResponseEntity.ok(sistemaService.listarTodos());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        sistemaService.eliminarSistema(id);
        return ResponseEntity.noContent().build();
    }

}
