package com.auth.auth.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreDepartamento;

    @ManyToOne
    private Departamento departamentoSuperior;

    private boolean activo;

    @ManyToOne
    private Persona jefe;

    @Enumerated(EnumType.STRING)
    private NivelDepartamento nivel;

    @OneToMany(mappedBy = "departamentoSuperior")
    private List<Departamento> childrens = new ArrayList<>();

    public enum NivelDepartamento {
        ALCALDIA,
        ADMINISTRACION,
        DIRECCION,
        SUBDIRECCION,
        DEPARTAMENTO,
        SECCION,
        OFICINA

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public Departamento getDepartamentoSuperior() {
        return departamentoSuperior;
    }

    public void setDepartamentoSuperior(Departamento departamentoSuperior) {
        this.departamentoSuperior = departamentoSuperior;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Persona getJefe() {
        return jefe;
    }

    public void setJefe(Persona jefe) {
        this.jefe = jefe;
    }

    public NivelDepartamento getNivel() {
        return nivel;
    }

    public void setNivel(NivelDepartamento nivel) {
        this.nivel = nivel;
    }

    public List<Departamento> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<Departamento> childrens) {
        this.childrens = childrens;
    }

}
