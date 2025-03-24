package com.auth.auth.services.interfaces;

import com.auth.auth.entities.PasswordResetToken;

public interface PasswordResetTokenService {

    PasswordResetToken save(PasswordResetToken passwordResetToken);

    PasswordResetToken getByToken(String token);

    void delete(PasswordResetToken passwordResetToken);

}
