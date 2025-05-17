package com.auth.auth.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.auth.dto.PermisoRequest;
import com.auth.auth.exceptions.PermisoException;
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
    public ResponseEntity<Object> createPermioso(@RequestBody PermisoRequest request) {
        try {
            return ResponseEntity
                    .ok(permisoService.createPermiso(request.getNombrePermiso(), request.getCodigoSistema()));

        } catch (PermisoException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

}
