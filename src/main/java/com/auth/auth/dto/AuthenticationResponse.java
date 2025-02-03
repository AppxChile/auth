package com.auth.auth.dto;

public class AuthenticationResponse {

    private String token;
    private Boolean successful;

    
    public AuthenticationResponse(String token,  Boolean successful) {
        this.token = token;
        this.successful = successful;
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


    


}
