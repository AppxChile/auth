package com.auth.auth.services;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.auth.api.PersonaResponse;
import com.auth.auth.configuration.ApiProperties;
import com.auth.auth.entities.PasswordResetToken;
import com.auth.auth.entities.Persona;
import com.auth.auth.entities.Usuario;
import com.auth.auth.exceptions.SendMailExceptions;
import com.auth.auth.repositories.PasswordResetTokenRepository;
import com.auth.auth.repositories.PersonaRepository;
import com.auth.auth.repositories.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class PasswordRecoveryService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository tokenRepository;
    private final PersonaRepository personaRepository;
    private final ApiService apiService;
    private final ApiProperties apiProperties;

    public PasswordRecoveryService(UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,

            PasswordResetTokenRepository tokenRepository,
            PersonaRepository personaRepository,
            ApiService apiService,
            ApiProperties apiProperties) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.personaRepository = personaRepository;
        this.apiService = apiService;
        this.apiProperties = apiProperties;
    }

    public void sendRecoveryEmail(Integer rut) {

        Persona persona = personaRepository.findByRut(rut)
                .orElseThrow(() -> new IllegalArgumentException("Persona no encontrada" + rut));

        // Buscar al usuario por RUT
        Usuario usuario = usuarioRepository.findByPersona(persona)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado para el RUT: " + rut));

        // Generar un token único y temporal
        String token = UUID.randomUUID().toString();

        // Guardar el token en la base de datos
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, usuario);
        tokenRepository.save(passwordResetToken);

        // Crear un enlace de recuperación
        String recoveryLink = apiProperties.getRecoveryUrl() + token;

        PersonaResponse personaResponse = apiService.getPersonaInfo(rut);

        Map<String, Object> variables = Map.of(
                "nombre", personaResponse.getNombres(),
                "recoveryLink", recoveryLink);

        try {
            apiService.sendEmail(personaResponse.getEmail(), "Recuperacion de contraseña", "recovery-template",
                    variables);
        } catch (SendMailExceptions e) {
            throw new SendMailExceptions("Error enviando correo de activación a " + personaResponse.getEmail());
        }

    }

    @Transactional
    public void resetPassword(String token, String newPassword) {

        PasswordResetToken passwordResetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido o expirado"));

        if (passwordResetToken.isExpired()) {
            throw new IllegalArgumentException("El token ha expirado");
        }

        Usuario usuario = passwordResetToken.getUsuario();

        String encodedPassword = passwordEncoder.encode(newPassword);
        usuario.setPassword(encodedPassword);
        usuarioRepository.save(usuario);

        tokenRepository.delete(passwordResetToken);

    }

}
