package com.auth.auth.dto;

public class AuthenticationResponse {

    private String token;
    private Boolean successful;
    private Boolean func;

    
    public AuthenticationResponse(String token,  Boolean successful, Boolean func) {
        this.token = token;
        this.successful = successful;
        this.func = func;
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


    


}
