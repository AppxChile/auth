package com.auth.auth.dto;

public class PermisoDto {

    private Long id;
    private String nombre;

    public PermisoDto() {
    }

    public PermisoDto(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
