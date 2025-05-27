package com.auth.auth.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.auth.auth.dto.ApiError;
import com.auth.auth.dto.ModuloRequest;
import com.auth.auth.dto.ModuloResponse;
import com.auth.auth.services.interfaces.ModuloService;

@RestController
@RequestMapping("/api/auth/modulos")
public class ModuloController {

    private final ModuloService moduloService;

    public ModuloController(ModuloService moduloService) {
        this.moduloService = moduloService;
    }

    @PostMapping
    public ResponseEntity<Object> crearModulo(@RequestBody ModuloRequest request) {
        try {
            ModuloResponse modulo = moduloService.crearModulo(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(modulo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiError("Datos inválidos", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError("Error interno", e.getMessage()));
        }
    }
}