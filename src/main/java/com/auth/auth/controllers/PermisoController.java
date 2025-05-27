package com.auth.auth.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.auth.dto.ApiError;
import com.auth.auth.dto.PermisoRequest;
import com.auth.auth.dto.PermisoResponse;
import com.auth.auth.services.interfaces.PermisoService;

@RestController
@RequestMapping("/api/auth/usuarios/permisos")
@CrossOrigin(origins = "http://localhost:5173")
public class PermisoController {

    private final PermisoService permisoService;

    public PermisoController(PermisoService permisoService) {
        this.permisoService = permisoService;
    }

   @PostMapping
    public ResponseEntity<Object> crearPermiso(@RequestBody PermisoRequest request) {
        try {
            PermisoResponse permiso = permisoService.crearPermiso(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(permiso);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiError("Datos inválidos", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiError("Error interno", e.getMessage()));
        }
    }

}
