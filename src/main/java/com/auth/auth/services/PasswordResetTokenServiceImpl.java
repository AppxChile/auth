package com.auth.auth.services;

import org.springframework.stereotype.Service;

import com.auth.auth.entities.PasswordResetToken;
import com.auth.auth.repositories.PasswordResetTokenRepository;
import com.auth.auth.services.interfaces.PasswordResetTokenService;
import com.auth.auth.utils.RepositoryUtils;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public PasswordResetTokenServiceImpl(PasswordResetTokenRepository passwordResetTokenRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    @Override
    public PasswordResetToken save(PasswordResetToken passwordResetToken) {
        return passwordResetTokenRepository.save(passwordResetToken);

    }

    @Override
    public PasswordResetToken getByToken(String token) {

        return RepositoryUtils.findOrThrow(passwordResetTokenRepository.findByToken(token),
                String.format("Token %s no encontrado", token));
    }

    @Override
    public void delete(PasswordResetToken passwordResetToken) {
        passwordResetTokenRepository.delete(passwordResetToken);

    }

}
