package com.auth.auth.services.interfaces;

import com.auth.auth.dto.PasswordRequest;

public interface PasswordRecoveryService {

    void sendRecoveryEmail(Integer rut);

    void resetPassword(String token, PasswordRequest newPassword);

}
