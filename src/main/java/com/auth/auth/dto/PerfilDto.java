package com.auth.auth.dto;

import java.util.Set;

public class PerfilDto {

    private Long id;
    private String nombre;
    private Set<SistemaDto> sistemas;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public Set<SistemaDto> getSistemas() {
        return sistemas;
    }
    public void setSistemas(Set<SistemaDto> sistemas) {
        this.sistemas = sistemas;
    }

   


 

}
