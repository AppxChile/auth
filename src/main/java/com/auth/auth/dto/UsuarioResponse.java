package com.auth.auth.dto;

public class UsuarioResponse {

    private String username;
    private String activationToken;
    private Long idDepartamento;

    public UsuarioResponse() {
    }

    public UsuarioResponse(String username) {
        this.username = username;
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

}
