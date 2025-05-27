package com.auth.auth.dto;

import java.util.Set;

public class PerfilResponse {
    private Long id;
    private String nombre;
    private Set<String> sistemas;

    public PerfilResponse(Long id, String nombre, Set<String> sistemas) {
        this.id = id;
        this.nombre = nombre;
        this.sistemas = sistemas;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Set<String> getSistemas() {
        return sistemas;
    }
}
