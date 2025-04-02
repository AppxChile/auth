package com.auth.auth.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.auth.entities.Departamento;
import com.auth.auth.services.interfaces.DepartamentoService;

@RestController
@RequestMapping("/api/auth/usuarios/departamentos")
@CrossOrigin(origins = { "https://dev.appx.cl/", "http//localhost:5173" })
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    @GetMapping("/list")
    public ResponseEntity<Object> getDepartamentos() {
        try {
            List<Departamento> deptos = departamentoService.getAll();
            return ResponseEntity.ok().body(deptos);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/byId/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id) {
        try {
            Departamento depto = departamentoService.getById(id);
            return ResponseEntity.ok().body(depto);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
