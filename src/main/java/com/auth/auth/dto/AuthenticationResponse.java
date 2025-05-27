package com.auth.auth.dto;

import java.util.List;

public class AuthenticationResponse {

    private String token;
    private Boolean successful;
    private Boolean func;
    private List<PerfilDto> perfil;

    public AuthenticationResponse(String token, Boolean successful, Boolean func) {
        this.token = token;
        this.successful = successful;
        this.func = func;
    }

    public AuthenticationResponse(String token, Boolean successful, Boolean func, List<PerfilDto> perfil) {
        this.token = token;
        this.successful = successful;
        this.func = func;
        this.perfil = perfil;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public Boolean getFunc() {
        return func;
    }

    public void setFunc(Boolean func) {
        this.func = func;
    }

    public List<PerfilDto> getPerfil() {
        return perfil;
    }

    public void setPerfil(List<PerfilDto> perfil) {
        this.perfil = perfil;
    }

}
