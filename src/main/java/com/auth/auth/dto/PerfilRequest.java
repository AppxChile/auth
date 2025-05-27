package com.auth.auth.dto;

import java.util.Set;

public class PerfilRequest {
    private String nombre;
    private Set<Long> sistemaIds;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Long> getSistemaIds() {
        return sistemaIds;
    }

    public void setSistemaIds(Set<Long> sistemaIds) {
        this.sistemaIds = sistemaIds;
    }
}
