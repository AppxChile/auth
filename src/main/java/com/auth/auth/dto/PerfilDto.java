package com.auth.auth.dto;

import java.util.List;
public class PerfilDto {

    private String nombre;
    private List<PermisoDto> permisos;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<PermisoDto> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<PermisoDto> permisos) {
        this.permisos = permisos;
    }


    
}
