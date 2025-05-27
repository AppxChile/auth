package com.auth.auth.dto;

import java.util.Set;

public class ModuloDto {

    private Long idModulo;
    private String nombreModulo;
    private Set<PermisoDto> permisos;

    public Long getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(Long idModulo) {
        this.idModulo = idModulo;
    }

    public String getNombreModulo() {
        return nombreModulo;
    }

    public void setNombreModulo(String nombreModulo) {
        this.nombreModulo = nombreModulo;
    }

    public Set<PermisoDto> getPermisos() {
        return permisos;
    }

    public void setPermisos(Set<PermisoDto> permisos) {
        this.permisos = permisos;
    }

}
