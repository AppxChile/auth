package com.auth.auth.dto;

public class ModuloResponse {

    private Long id;
    private String codigo;
    private String nombre;
    private String sistemaNombre;

    public ModuloResponse(Long id, String codigo, String nombre, String sistemaNombre) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.sistemaNombre = sistemaNombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSistemaNombre() {
        return sistemaNombre;
    }

    public void setSistemaNombre(String sistemaNombre) {
        this.sistemaNombre = sistemaNombre;
    }

}
