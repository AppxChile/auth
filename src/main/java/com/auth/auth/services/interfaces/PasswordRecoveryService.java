package com.auth.auth.services.interfaces;

public interface PasswordRecoveryService {

    void sendRecoveryEmail(Integer rut);

    void resetPassword(String token, String newPassword);


}
