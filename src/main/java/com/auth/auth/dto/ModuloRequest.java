package com.auth.auth.dto;

public class ModuloRequest {

    private String codigo;
    private String nombre;
    private Long sistemaId;

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

    public Long getSistemaId() {
        return sistemaId;
    }

    public void setSistemaId(Long sistemaId) {
        this.sistemaId = sistemaId;
    }

}
