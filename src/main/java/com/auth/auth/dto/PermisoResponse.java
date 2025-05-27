package com.auth.auth.dto;

public class PermisoResponse {

    private Long id;
    private String nombre;
    private String sistemaCodigo;
    private String moduloCodigo;

    public PermisoResponse(Long id, String nombre, String sistemaCodigo, String moduloCodigo) {
        this.id = id;
        this.nombre = nombre;
        this.sistemaCodigo = sistemaCodigo;
        this.moduloCodigo = moduloCodigo;
    }

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

    public String getSistemaCodigo() {
        return sistemaCodigo;
    }

    public void setSistemaCodigo(String sistemaCodigo) {
        this.sistemaCodigo = sistemaCodigo;
    }

    public String getModuloCodigo() {
        return moduloCodigo;
    }

    public void setModuloCodigo(String moduloCodigo) {
        this.moduloCodigo = moduloCodigo;
    }

}
