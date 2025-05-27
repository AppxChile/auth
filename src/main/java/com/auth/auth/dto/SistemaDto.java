package com.auth.auth.dto;

import java.util.Set;

public class SistemaDto {

    private String codSistema;
    private String nombreSistema;
    private Set<ModuloDto> modulos;

    public String getCodSistema() {
        return codSistema;
    }

    public void setCodSistema(String codSistema) {
        this.codSistema = codSistema;
    }

    public String getNombreSistema() {
        return nombreSistema;
    }

    public void setNombreSistema(String nombreSistema) {
        this.nombreSistema = nombreSistema;
    }

    public Set<ModuloDto> getModulos() {
        return modulos;
    }

    public void setModulos(Set<ModuloDto> modulos) {
        this.modulos = modulos;
    }

}
