package com.auth.auth.dto;

import java.util.List;

public class UsuarioResponse {

    private String username;
    private String activationToken;
    private Long idDepartamento;
    private Boolean isFunc;

    private List<PerfilDto> perfiles;

    public UsuarioResponse() {
    }

    public UsuarioResponse(String activationToken, Boolean isFunc, List<PerfilDto> perfiles) {
        this.activationToken = activationToken;
        this.isFunc = isFunc;
        this.perfiles = perfiles;
    }

    public UsuarioResponse(String activationToken, Boolean isFunc) {
        this.activationToken = activationToken;
        this.isFunc = isFunc;
    }

    public UsuarioResponse(String username) {
        this.username = username;
    }

    public UsuarioResponse(String username, String activationToken, Boolean isFunc) {
        this.username = username;
        this.activationToken = activationToken;
        this.isFunc = isFunc;
    }

    public UsuarioResponse(String username, String activationToken) {
        this.username = username;
        this.activationToken = activationToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }

    public Long getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(Long idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public List<PerfilDto> getPerfiles() {
        return perfiles;
    }

    public void setPerfiles(List<PerfilDto> perfiles) {
        this.perfiles = perfiles;
    }

    public Boolean getIsFunc() {
        return isFunc;
    }

    public void setIsFunc(Boolean isFunc) {
        this.isFunc = isFunc;
    }

}
